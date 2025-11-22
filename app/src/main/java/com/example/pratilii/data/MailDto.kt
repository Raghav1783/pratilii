package com.example.pratilii.data

import com.example.pratilii.domain.Mail
import com.google.gson.annotations.SerializedName

import kotlinx.serialization.Serializable

@Serializable
data class MailDto(
    val id: Int,
    val subject: String,
    val sender: String,
    val readCount: Int
){
    fun toDomain(): Mail {
        return Mail(
            id = id,
            subject = subject,
            sender = sender,
            readCount = readCount
        )
    }

}
