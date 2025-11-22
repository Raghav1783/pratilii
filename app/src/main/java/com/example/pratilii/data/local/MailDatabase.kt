package com.example.pratilii.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MailEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MailDatabase: RoomDatabase() {
    abstract fun mailDao(): MailDao
}