package com.kasandi.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kasandi.storyapp.data.StoryRepository
import com.kasandi.storyapp.data.UserRepository
import com.kasandi.storyapp.data.remote.response.Story
import kotlinx.coroutines.launch

class MainViewModel(storyRepository: StoryRepository, private val userRepository: UserRepository
) : ViewModel() {

    val stories: LiveData<PagingData<Story>> =
        storyRepository.getStoriesData().cachedIn(viewModelScope)

    fun logout() = viewModelScope.launch {
        userRepository.logout()
    }
}