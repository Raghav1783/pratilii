package com.example.pratilii.presentation.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pratilii.domain.usecase.IncrementReadCountUseCase
import com.example.pratilii.domain.Mail
import com.example.pratilii.domain.usecase.fetchMailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MailListViewModel @Inject constructor(
    fetchMailUseCase: fetchMailUseCase,
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

sealed interface MailListUiEvent {
    data class OnMailSelected(val mailId: Int) :MailListUiEvent
}


