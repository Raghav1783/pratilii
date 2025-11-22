package com.example.pratilii.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pratilii.data.MailDto
import com.example.pratilii.domain.Mail

@Entity(tableName = "mail")
data class MailEntity(
    @PrimaryKey
    val id: Int,
   val subject: String,
   val sender: String,
   val readCount: Int,
    val isSynced: Boolean = true
    )

fun Mail.toEntity(): MailEntity {
    return MailEntity(
        id = id,
        subject = subject,
        sender = sender,
        readCount = readCount,
    )
}

fun MailEntity.toDomain(): Mail {
    return Mail(
        id = id,
        subject = subject,
        sender = sender,
        readCount = readCount
    )
}

fun MailEntity.toDto() = MailDto(
    id = id,
    subject = subject,
    sender = sender,
    readCount = readCount
)