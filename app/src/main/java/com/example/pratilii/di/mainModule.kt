package com.example.pratilii.di

import android.content.Context
import androidx.room.Room
import com.example.pratilii.data.ApiSimulator
import com.example.pratilii.data.MailRepoImpl
import com.example.pratilii.data.local.MailDao
import com.example.pratilii.data.local.MailDatabase
import com.example.pratilii.domain.MailRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object mainModule {

    @Provides
    @Singleton
    fun bindMailRepository(
        mailRepoImpl: MailRepoImpl
    ): MailRepository {
        return mailRepoImpl
    }

    @Provides
    @Singleton
    fun provideApiService( @ApplicationContext context: Context): ApiSimulator {
        val apiService = ApiSimulator(context)
        return apiService
    }


    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MailDatabase {
        return Room.databaseBuilder(
            context,
            MailDatabase::class.java,
            "app_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMailDao(db: MailDatabase): MailDao {
        return db.mailDao()
    }
}