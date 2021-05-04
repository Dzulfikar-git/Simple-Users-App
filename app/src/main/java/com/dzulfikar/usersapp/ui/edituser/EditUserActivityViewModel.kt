package com.dzulfikar.usersapp.ui.edituser

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzulfikar.usersapp.data.repository.AppRepository
import com.dzulfikar.usersapp.data.source.entities.Users
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditUserActivityViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {
    fun getUser(uid: Int) : LiveData<Users> = repository.getDetailUser(uid)

    fun saveNewData(user:Users) = viewModelScope.launch {
        repository.updateUser(user)
    }
}