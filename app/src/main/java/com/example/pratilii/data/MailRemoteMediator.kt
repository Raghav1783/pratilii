package com.example.pratilii.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.pratilii.data.local.MailDatabase
import com.example.pratilii.data.local.MailEntity
import com.example.pratilii.data.local.toDomain
import com.example.pratilii.data.local.toDto
import com.example.pratilii.data.local.toEntity
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MailRemoteMediator @Inject constructor(
    private val apiSimulator: ApiSimulator,
    private val database: MailDatabase,
) : RemoteMediator<Int, MailEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MailEntity>
    ): MediatorResult {

        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    syncUnsyncedData()
                    0
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        0
                    } else {
                        val currentPage =
                            (state.pages.sumOf { it.data.size } / state.config.pageSize)

                        currentPage
                    }
                }
            }

            val pageSize = state.config.pageSize

            val response = apiSimulator.getMessagesDto(page, pageSize)
            val domainMessages = response.data.map { it.toDomain() }
            val entities = domainMessages.map { it.toEntity() }

            val endOfPagination = entities.isEmpty() || page >= response.totalPages - 1

            database.withTransaction {
                database.mailDao().insertAll(entities)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPagination)

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun syncUnsyncedData() {
        try {

            val unsyncedData = database.mailDao().getUnsyncedMails()

            if (unsyncedData.isNotEmpty()) {
                apiSimulator.updateMails(unsyncedData.map { it.toDto() })
            }

            database.mailDao().clearAll()

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}