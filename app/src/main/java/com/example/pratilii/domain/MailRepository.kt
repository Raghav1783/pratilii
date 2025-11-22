package com.example.pratilii.domain

import androidx.paging.PagingData
import com.example.pratilii.util.Resource

import kotlinx.coroutines.flow.Flow

interface MailRepository {
    fun getMessagesPaged(): Flow<PagingData<Mail>>

    suspend fun incrementReadCount(mailId: Int)

    suspend fun fetchMailDetail(mailId: Int): Resource<Mail>
}