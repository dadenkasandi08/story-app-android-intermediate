package com.kasandi.storyapp.data.local.preference

data class UserModel(
    var email: String,
    var token: String,
    var isLogin: Boolean = false
)
