package com.dzulfikar.usersapp.ui.detailuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzulfikar.usersapp.data.repository.AppRepository
import com.dzulfikar.usersapp.data.source.entities.Users
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailUserViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {
    fun getDetailUser(userId: Int) : LiveData<Users> = repository.getDetailUser(userId)

    fun deleteUser(userId: Int) = viewModelScope.launch {
        repository.deleteUser(userId)
    }

    fun getRandomUsersFiltered(userId: Int) : LiveData<List<Users>> = repository.getRandomUsersFiltered(userId)
}