package com.example.pratilii.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pratilii.Resource
import com.example.pratilii.domain.Mail
import com.example.pratilii.domain.fetchDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel@Inject constructor(
    val fetchDetailUseCase: fetchDetailUseCase,
    savedStateHandle: SavedStateHandle
):ViewModel() {

    private val _mailDetail = MutableStateFlow<Resource<Mail>>(Resource.Loading)
    val mailDetail: StateFlow<Resource<Mail>> = _mailDetail

    init {
        val mailId = savedStateHandle.get<Int>("id") ?: 0
        loadMailDetail(mailId)
    }

    fun loadMailDetail(mailId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _mailDetail.value = fetchDetailUseCase(mailId)
        }
    }
}