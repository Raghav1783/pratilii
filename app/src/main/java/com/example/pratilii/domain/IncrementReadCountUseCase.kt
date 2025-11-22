package com.example.pratilii.domain

import javax.inject.Inject

class IncrementReadCountUseCase @Inject constructor(
    private val repository: MailRepository
) {
    suspend operator fun invoke(mailId: Int) {
        repository.incrementReadCount(mailId)
    }
}