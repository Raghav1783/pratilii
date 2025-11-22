package com.example.pratilii.presentation.home

import android.util.Log
import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.pratilii.domain.Mail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MailListScreen(onMailClick: (Int) -> Unit) {
    val viewModel: MailListViewModel = hiltViewModel()

    val pagingItems = viewModel.mailsPagingFlow.collectAsLazyPagingItems()


    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is MailListUiEvent.OnMailSelected -> {
                    onMailClick(event.mailId)

                }
            }
        }
    }

    val refreshState = pagingItems.loadState.refresh
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshState is LoadState.Loading,
        onRefresh = { pagingItems.refresh() }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        when (refreshState) {
            is LoadState.Loading -> {
            }

            is LoadState.Error -> {
                Log.d("Mailscreen ","outer error")
                val errorMessage = refreshState.error.localizedMessage ?: "Something went wrong"
                ErrorState(
                    message = errorMessage,
                    onRetry = { pagingItems.retry() }
                )
            }

            is LoadState.NotLoading -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(
                        count = pagingItems.itemCount,
                        key = { index -> pagingItems[index]?.id ?: index }
                    ) { index ->
                        val item = pagingItems[index]
                        item?.let { mail ->

                            MailListItem(mail,
                                onClick = { viewModel.onAction(MailListUiAction.OnMailSelected(mail.id)) })
                            Divider()
                        }
                    }
                    when(val state = pagingItems.loadState.append){
                        is LoadState.Error -> {
                            item {
                                Log.d("Mailscreen ","inner error")
                                val errorMessage =  state.error.localizedMessage ?: "Something went wrong"
                                ErrorState(
                                    message = errorMessage,
                                    onRetry = { pagingItems.retry() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }
                        }
                        LoadState.Loading ->{
                            item {
                            LoadingState()
                            }
                        }
                        is LoadState.NotLoading -> {
                        }
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = refreshState is LoadState.Loading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }

}

@Composable
fun MailListItem(mail: Mail, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    ) {
        Text(text = mail.subject)
        Text(text = mail.sender)
        Text(text = mail.readCount.toString())
    }
}

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorState( message :String, modifier: Modifier = Modifier,onRetry: () -> Unit) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.clip(RoundedCornerShape(6.dp))
                    .background(Color.LightGray)
                    .clickable { onRetry() }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Retry")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(message, color = MaterialTheme.colors.error)
        }
    }
}

sealed interface MailListUiAction {
    data class OnMailSelected(val mailid: Int) : MailListUiAction
}