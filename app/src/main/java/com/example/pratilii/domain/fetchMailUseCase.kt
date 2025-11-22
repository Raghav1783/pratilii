package com.example.pratilii.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class fetchMailUseCase @Inject constructor(
    private val apiRepository: MailRepository
) {
    operator fun invoke(isSeen: Boolean? = null): Flow<PagingData<Mail>> {
        return apiRepository.getMessagesPaged()
    }
}