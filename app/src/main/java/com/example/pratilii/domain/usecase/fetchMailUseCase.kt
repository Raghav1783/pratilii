package com.example.pratilii.domain.usecase

import androidx.paging.PagingData
import com.example.pratilii.domain.Mail
import com.example.pratilii.domain.MailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class fetchMailUseCase @Inject constructor(
    private val apiRepository: MailRepository
) {
    operator fun invoke(isSeen: Boolean? = null): Flow<PagingData<Mail>> {
        return apiRepository.getMessagesPaged()
    }
}