package com.dzulfikar.usersapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.dzulfikar.usersapp.data.source.UsersDao
import com.dzulfikar.usersapp.data.source.entities.Users
import javax.inject.Inject

class AppRepository @Inject constructor(private val usersDao: UsersDao) {
    suspend fun addNewUser(users: Users){
        usersDao.insertNewUser(users)
        Log.d("Repository", "Inserted")
    }

    suspend fun updateUser(users: Users) = usersDao.updateUser(users)

    fun getAllUsers() : LiveData<List<Users>> = usersDao.getAllUsers()

    fun getDetailUser(userId: Int) : LiveData<Users> = usersDao.getUser(userId)

    suspend fun deleteUser(userId: Int) = usersDao.deleteUser(userId)

    fun getRandomUsersFiltered(userId: Int) : LiveData<List<Users>> = usersDao.getRandomUsersFiltered(userId)

    fun getSearchedUsers(name: String) : LiveData<List<Users>> = usersDao.getSearchedUsers(name)
}