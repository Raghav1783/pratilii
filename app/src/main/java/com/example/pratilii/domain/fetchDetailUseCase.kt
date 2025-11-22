package com.example.pratilii.domain

import androidx.paging.PagingData
import com.example.pratilii.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class fetchDetailUseCase @Inject constructor(
    private val apiRepository: MailRepository
) {
    suspend operator fun invoke(id:Int): Resource<Mail>{
        return apiRepository.fetchMailDetail(id)
    }
}