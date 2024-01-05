package com.kasandi.storyapp.data.remote.retrofit

import com.kasandi.storyapp.data.remote.response.ResponseDetailStory
import com.kasandi.storyapp.data.remote.response.ResponseLogin
import com.kasandi.storyapp.data.remote.response.ResponseRegister
import com.kasandi.storyapp.data.remote.response.ResponseStory
import com.kasandi.storyapp.data.remote.response.ResponseUpload
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseRegister>

    @GET("stories")
    fun getStoriesWithLocation(
        @Header("Authorization") token: String,
        @Query("location") location : Int = 1
    ): Call<ResponseStory>

    @GET("stories")
    fun getStories(
        @Header("Authorization") token: String,
    ): Call<ResponseStory>

    @GET("stories")
    suspend fun getMoreStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): ResponseStory


    @Multipart
    @POST("stories")
    fun uploadStory(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") latitude: RequestBody? = null,
        @Part("lon") longitude: RequestBody? = null
    ): Call<ResponseUpload>

    @GET("stories/{id}")
    fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<ResponseDetailStory>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseLogin>
}