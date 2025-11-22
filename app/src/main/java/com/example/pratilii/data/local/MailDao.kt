package com.example.pratilii.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MailDao {
    @Query("SELECT * FROM mail")
    fun pagingSource(): PagingSource<Int, MailEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mails: List<MailEntity>)

    @Query("DELETE FROM mail")
    suspend fun clearAll()

    @Query("UPDATE mail SET readCount = readCount + 1, isSynced = 0 WHERE id = :mailId")
    suspend fun incrementReadCount(mailId: Int)

    @Query("SELECT * FROM mail WHERE isSynced = 0")
    suspend fun getUnsyncedMails(): List<MailEntity>

    @Query("UPDATE mail SET isSynced = 1 WHERE id = :mailId")
    suspend fun markAsSynced(mailId: Int)

    @Query("SELECT * FROM mail WHERE id = :mailId")
    suspend fun fetchMailDetail(mailId: Int):MailEntity
}