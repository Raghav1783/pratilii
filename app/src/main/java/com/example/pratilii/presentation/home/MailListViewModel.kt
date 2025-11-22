package com.example.pratilii.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pratilii.domain.IncrementReadCountUseCase
import com.example.pratilii.domain.Mail
import com.example.pratilii.domain.fetchMailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MailListViewModel @Inject constructor(
    private val fetchMailUseCase: fetchMailUseCase,
    private val incrementReadCountUseCase: IncrementReadCountUseCase
): ViewModel() {



    private val _eventChannel = Channel<MailListUiEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    val mailsPagingFlow: Flow<PagingData<Mail>> = fetchMailUseCase()
        .cachedIn(viewModelScope)


    fun onAction(uiAction: MailListUiAction) {
        when (uiAction) {
            is MailListUiAction.OnMailSelected -> {
                incrementReadCount(uiAction.mailid)
                sendEvent(MailListUiEvent.OnMailSelected(uiAction.mailid))
            }

        }
    }

    private fun incrementReadCount(mailid: Int) {
        viewModelScope.launch(Dispatchers.IO){
            incrementReadCountUseCase(mailid)
        }
    }


    private fun sendEvent(event : MailListUiEvent) {
        viewModelScope.launch {
            _eventChannel.send(event)
        }
    }

}
sealed class MailUiState {
    data object Loading : MailUiState()

    data class Success(
        val mails: List<Mail>,
        val isRefreshing: Boolean = false
    ) : MailUiState()

    data class Error(
        val message: String,
        val isRefreshing: Boolean = false
    ) : MailUiState()
}

sealed interface MailListUiEvent {
    data class OnMailSelected(val mailId: Int) :MailListUiEvent
}


