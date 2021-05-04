package com.dzulfikar.usersapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dzulfikar.usersapp.data.source.UsersDao
import com.dzulfikar.usersapp.data.source.entities.Users

@Database(entities = [Users::class], version = 1)
abstract class UsersDatabase : RoomDatabase(){
    abstract fun usersDao() : UsersDao
}