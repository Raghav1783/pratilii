package com.example.pratilii.data

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject


class ApiSimulator @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var callCount = AtomicInteger(0)

    private val fakeData = mutableListOf<MailDto>()

    private val jsonFileName = "mail_data.json"

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val file = context.getFileStreamPath(jsonFileName)

        val jsonString = if (file.exists()) {
            file.readText()
        } else {
            context.assets.open(jsonFileName).bufferedReader().use { it.readText() }
        }

        val list = Json.decodeFromString<List<MailDto>>(jsonString)
        fakeData.clear()
        fakeData.addAll(list)
    }

    /** Persists the in-memory list to internal storage JSON */
    private fun saveDataToFile() {
        val json = Json.encodeToString(fakeData)
        context.openFileOutput(jsonFileName, Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }

    /** Add new item */
    fun addMail(subject: String, sender: String) {
        val newId = (fakeData.maxOfOrNull { it.id } ?: 0) + 1
        fakeData.add(
            MailDto(
                id = newId,
                subject = subject,
                sender = sender,
                readCount = 0
            )
        )
        saveDataToFile()
    }

    fun updateMails(dataList: List<MailDto>) {
        dataList.forEach { updatedMail ->
            // Find the index of the existing mail in fakeData
            val index = fakeData.indexOfFirst { it.id == updatedMail.id }

            if (index != -1) {
                val currentMail = fakeData[index]

                // Update only fields that are not null
                fakeData[index] = currentMail.copy(
                    subject = updatedMail.subject ,
                    sender = updatedMail.sender,
                    readCount = updatedMail.readCount
                )
            } else {
                // Optional: If mail does not exist, add it
                fakeData.add(updatedMail)
            }
        }

        // Persist to file
        saveDataToFile()
    }




    suspend fun getMessagesDto(page: Int, pageSize: Int): PaginatedResponse<MailDto> {
        val currentCount = callCount.incrementAndGet()
        Log.d("apisimulator"," network call")

        if (currentCount % 4 == 0) {
            throw java.io.IOException("Network error simulated")
        }


        val delayTimeMs = 5000L
        delay(delayTimeMs)

        val startIndex = page * pageSize
        val endIndex = minOf(startIndex + pageSize, fakeData.size)

        if (startIndex >= fakeData.size) {
            return PaginatedResponse(
                data = emptyList(),
                currentPage = page,
                totalPages = (fakeData.size + pageSize - 1) / pageSize,
                totalItems = fakeData.size
            )
        }

        return PaginatedResponse(
            data = fakeData.subList(startIndex, endIndex),
            currentPage = page,
            totalPages = (fakeData.size + pageSize - 1) / pageSize,
            totalItems = fakeData.size
        )

    }

}

data class PaginatedResponse<T>(
    val data: List<T>,
    val currentPage: Int,
    val totalPages: Int,
    val totalItems: Int
)