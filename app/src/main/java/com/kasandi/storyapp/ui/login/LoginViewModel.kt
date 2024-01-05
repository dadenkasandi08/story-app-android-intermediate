package com.kasandi.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kasandi.storyapp.data.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    fun startSession(
        email: String,
        password: String
    ) {
        repository.login(email, password)
    }

    fun getLoginResponse() = repository.getLoginResponse()

    fun saveSession() = viewModelScope.launch {
        repository.saveSession()
    }

    fun getSession() = repository.getSession().asLiveData()

}