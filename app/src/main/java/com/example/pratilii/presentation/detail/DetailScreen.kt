package com.example.pratilii.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pratilii.Resource
import com.example.pratilii.domain.Mail

@Composable
fun MailDetailScreen(viewModel: DetailScreenViewModel) {


    val mailResource by viewModel.mailDetail.collectAsState()

    when (mailResource) {
        is Resource.Loading -> CircularProgressIndicator()
        is Resource.Success -> {
            val mail = (mailResource as Resource.Success<Mail>).data
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
        is Resource.Error -> Text("Error: ${(mailResource as Resource.Error).message}")
    }
}