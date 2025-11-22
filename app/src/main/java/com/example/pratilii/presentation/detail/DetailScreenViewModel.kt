package com.example.pratilii.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pratilii.util.Resource
import com.example.pratilii.domain.Mail
import com.example.pratilii.domain.usecase.fetchDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel@Inject constructor(
    val fetchDetailUseCase: fetchDetailUseCase,
    savedStateHandle: SavedStateHandle
):ViewModel() {


    private val _state = MutableStateFlow<MailDetailState>(MailDetailState.Loading)
    val state: StateFlow<MailDetailState> = _state.asStateFlow()

    init {
        val mailId = savedStateHandle.get<Int>("id") ?: 0
        loadMailDetail(mailId)
    }

    private fun loadMailDetail(mailId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = MailDetailState.Loading

            when (val result = fetchDetailUseCase(mailId)) {
                is Resource.Success -> {
                    _state.value = MailDetailState.Success(result.data)
                }
                is Resource.Error -> {
                    _state.value = MailDetailState.Error(result.message ?: "Unknown error")
                }
                is Resource.Loading -> {
                    _state.value = MailDetailState.Loading
                }
            }
        }
    }
}

sealed interface MailDetailState {
    object Loading : MailDetailState
    data class Success(val mail: Mail) : MailDetailState
    data class Error(val message: String) : MailDetailState
}
