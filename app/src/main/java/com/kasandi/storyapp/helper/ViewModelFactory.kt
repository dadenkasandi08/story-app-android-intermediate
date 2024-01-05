package com.kasandi.storyapp.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kasandi.storyapp.data.StoryRepository
import com.kasandi.storyapp.data.UserRepository
import com.kasandi.storyapp.di.Injection
import com.kasandi.storyapp.ui.detail.DetailStoryViewModel
import com.kasandi.storyapp.ui.login.LoginViewModel
import com.kasandi.storyapp.ui.main.MainViewModel
import com.kasandi.storyapp.ui.map.MapsViewModel
import com.kasandi.storyapp.ui.signup.SignUpViewModel
import com.kasandi.storyapp.ui.upload.UploadViewModel

class ViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(storyRepository, userRepository) as T
        } else if (modelClass.isAssignableFrom(DetailStoryViewModel::class.java)) {
            return DetailStoryViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(UploadViewModel::class.java)) {
            return UploadViewModel(storyRepository) as T
        }else if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(storyRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(
                Injection.provideUserRepository(context),
                Injection.provideStoryRepository(context)
            )
        }.also {
            instance = it
        }
    }
}