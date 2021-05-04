package com.dzulfikar.usersapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dzulfikar.usersapp.data.repository.AppRepository
import com.dzulfikar.usersapp.data.source.entities.Users
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {
    fun getAllUsers() : LiveData<List<Users>> = repository.getAllUsers()

    fun getSearchedUsers(name: String) : LiveData<List<Users>> {
        val addedParams = "%$name%"
        return repository.getSearchedUsers(addedParams)
    }
}