package com.kasandi.storyapp.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasandi.storyapp.data.StoryRepository
import kotlinx.coroutines.launch

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun setStoriesWithLocation() = viewModelScope.launch {
        storyRepository.setStoriesWithLocation()
    }

    fun getStoriesWithLocation() = storyRepository.getStoriesWithLocation()
}