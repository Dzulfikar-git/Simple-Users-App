package com.dzulfikar.usersapp.data.source

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.dzulfikar.usersapp.data.source.entities.Users

@Dao
interface UsersDao {
    @Query("SELECT  * FROM Users")
    fun getAllUsers() : LiveData<List<Users>>

    @Insert
    suspend fun insertNewUser(users: Users)

    @Update
    suspend fun updateUser(users: Users)

    @Query("DELETE FROM Users WHERE uid =:userId ")
    suspend fun deleteUser(userId: Int)

    @Query("SELECT * FROM Users WHERE uid =:userId")
    fun getUser(userId: Int) : LiveData<Users>

    @Query("SELECT * FROM Users WHERE uid !=:userId ORDER BY RANDOM() LIMIT 3")
    fun getRandomUsersFiltered(userId: Int) : LiveData<List<Users>>

    @Query("SELECT * FROM Users WHERE first_name LIKE :name OR last_name LIKE :name OR first_name AND last_name LIKE :name")
    fun getSearchedUsers(name: String) : LiveData<List<Users>>
}