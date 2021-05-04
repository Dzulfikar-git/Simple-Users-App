package com.dzulfikar.usersapp.di

import android.content.Context
import androidx.room.Room
import com.dzulfikar.usersapp.data.database.UsersDatabase
import com.dzulfikar.usersapp.data.source.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Singleton
    @Provides
    fun provideUsersDatabase(@ApplicationContext appContext: Context): UsersDatabase {
        return Room.databaseBuilder(
            appContext,
            UsersDatabase::class.java,
            "users.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideUsersDao(database: UsersDatabase): UsersDao{
        return database.usersDao()
    }
}