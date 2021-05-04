package com.dzulfikar.usersapp.ui.adduser

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzulfikar.usersapp.data.repository.AppRepository
import com.dzulfikar.usersapp.data.source.entities.Users
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    fun addNewUser(users: Users) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNewUser(users)
        }
        Log.d("ViewModel", "Inserted")
    }

}