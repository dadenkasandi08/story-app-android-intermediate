package com.kasandi.storyapp.data

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import com.kasandi.storyapp.data.local.preference.UserModel
import com.kasandi.storyapp.data.local.preference.UserPreference
import com.kasandi.storyapp.data.remote.response.ResponseLogin
import com.kasandi.storyapp.data.remote.response.ResponseRegister
import com.kasandi.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    private var userModel: UserModel = UserModel("", "", false)
    private val registerResult = MediatorLiveData<Result<ResponseRegister>>()
    private val loginResult = MediatorLiveData<Result<ResponseLogin>>()


    fun getRegisterResponse() = registerResult

    fun getLoginResponse() = loginResult

    fun login(email: String, password: String) {
        loginResult.value = Result.Loading
        userModel = UserModel("", "", false)
        val client = apiService.login(email, password)
        client.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if (response.isSuccessful) {
                    val token = response.body()?.loginResult?.token
                    userModel.isLogin = true
                    userModel.email = email
                    userModel.token = token.toString()

                    loginResult.value = Result.Success(response.body()!!)
                } else {
                    loginResult.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                loginResult.value = Result.Error("No internet")
            }
        })
    }


    fun register(name: String, email: String, password: String) {
        registerResult.value = Result.Loading
        val client = apiService.register(name, email, password)
        client.enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(
                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                if (response.isSuccessful) {
                    registerResult.value = Result.Success(response.body()!!)
                } else {
                    registerResult.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                registerResult.value = Result.Error("No internet")
            }
        })
    }

    suspend fun saveSession() {
        userPreference.saveSession(userModel)
    }

    fun getSession(): Flow<UserModel> = this.userPreference.getSession()

    suspend fun logout() {
        this.userPreference.logout()
    }

    companion object {
        private const val TAG = "UserRepository"
    }
}