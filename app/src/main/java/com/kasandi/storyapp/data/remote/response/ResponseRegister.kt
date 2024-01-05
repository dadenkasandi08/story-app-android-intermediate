package com.kasandi.storyapp.data.remote.response


import com.google.gson.annotations.SerializedName

data class ResponseRegister(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)