package com.kasandi.storyapp.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasandi.storyapp.data.UserRepository
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) = viewModelScope.launch {
        repository.register(name, email, password)
    }

    fun getRegisterResponse() = repository.getRegisterResponse()
}
