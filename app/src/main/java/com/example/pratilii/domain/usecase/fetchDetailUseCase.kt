package com.example.pratilii.domain.usecase

import com.example.pratilii.util.Resource
import com.example.pratilii.domain.Mail
import com.example.pratilii.domain.MailRepository
import javax.inject.Inject

class fetchDetailUseCase @Inject constructor(
    private val apiRepository: MailRepository
) {
    suspend operator fun invoke(id:Int): Resource<Mail> {
        return apiRepository.fetchMailDetail(id)
    }
}