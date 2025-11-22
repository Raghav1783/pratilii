package com.example.pratilii.data

import android.os.Message
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.pratilii.Resource
import com.example.pratilii.data.local.MailDao
import com.example.pratilii.data.local.MailDatabase
import com.example.pratilii.data.local.toDomain
import com.example.pratilii.domain.Mail
import com.example.pratilii.domain.MailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MailRepoImpl @Inject constructor(
    private val apiService: ApiSimulator,
    private val database: MailDatabase
): MailRepository {


    @OptIn(ExperimentalPagingApi::class)
    override fun getMessagesPaged(): Flow<PagingData<Mail>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            remoteMediator = MailRemoteMediator(
                apiSimulator = apiService,
                database = database
            ),
            pagingSourceFactory = { database.mailDao().pagingSource() }
        ).flow.map{ pagingData ->
            pagingData.map { mailEntity -> mailEntity.toDomain() } }
    }

    override suspend fun incrementReadCount(mailId: Int) {
        database.mailDao().incrementReadCount(mailId)
    }

    override suspend fun fetchMailDetail(mailId: Int): Resource<Mail> {
        return try {
            val mailEntity = database.mailDao().fetchMailDetail(mailId)
            if (mailEntity != null) {
                Resource.Success(mailEntity.toDomain())
            } else {
                Resource.Error("Mail not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

}
