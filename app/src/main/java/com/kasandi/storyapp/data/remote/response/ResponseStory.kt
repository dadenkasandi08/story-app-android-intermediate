package com.kasandi.storyapp.data.remote.response


import com.google.gson.annotations.SerializedName

data class ResponseStory(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("listStory")
    val listStory: List<Story> = emptyList(),
    @SerializedName("message")
    val message: String
)