package com.example.pratilii.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pratilii.domain.Mail

@Composable
fun MailDetailScreen(viewModel: DetailScreenViewModel) {


    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state) {
        is MailDetailState.Loading -> {

        }
        is MailDetailState.Error -> {
            Text("Error: ${ (state as MailDetailState.Error).message}")
        }
        is MailDetailState.Success -> {
            MailContent(mail = (state as MailDetailState.Success).mail)
        }
    }
}

@Composable
fun MailContent(mail: Mail) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Name: ${mail.sender}",
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Subject: ${mail.subject}",
            style = MaterialTheme.typography.h2
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}