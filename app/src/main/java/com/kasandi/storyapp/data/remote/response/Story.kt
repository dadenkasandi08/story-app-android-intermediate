package com.kasandi.storyapp.data.remote.response


import com.google.gson.annotations.SerializedName

data class Story(
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("lon")
    val lon: Double? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("photoUrl")
    val photoUrl: String? = null
)