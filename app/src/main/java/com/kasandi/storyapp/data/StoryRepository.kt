package com.kasandi.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.kasandi.storyapp.data.local.preference.UserPreference
import com.kasandi.storyapp.data.remote.StoryPagingSource
import com.kasandi.storyapp.data.remote.response.ResponseDetailStory
import com.kasandi.storyapp.data.remote.response.ResponseStory
import com.kasandi.storyapp.data.remote.response.ResponseUpload
import com.kasandi.storyapp.data.remote.response.Story
import com.kasandi.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    private val uploadResult = MediatorLiveData<Result<ResponseUpload>>()
    private val detailStoryResult = MediatorLiveData<Result<Story>>()
    private val storiesWithLocationResult = MediatorLiveData<Result<List<Story>>>()

    suspend fun setStoriesWithLocation() {
        storiesWithLocationResult.value = Result.Loading
        val token = runBlocking { userPreference.getSession().first().token }
        val bearerToken = "Bearer $token"

        val client = apiService.getStoriesWithLocation(bearerToken, 1)
        client.enqueue(object : Callback<ResponseStory> {
            override fun onResponse(call: Call<ResponseStory>, response: Response<ResponseStory>) {
                if (response.isSuccessful) {
                    storiesWithLocationResult.value = Result.Success(response.body()!!.listStory)
                } else {
                    storiesWithLocationResult.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<ResponseStory>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                storiesWithLocationResult.value = Result.Error("No internet")
            }
        })
    }

    fun setDetailStory(id: String) {
        detailStoryResult.value = Result.Loading
        val token = runBlocking { userPreference.getSession().first().token }
        val bearerToken = "Bearer $token"
        val client = apiService.getDetailStory(bearerToken, id)
        client.enqueue(object : Callback<ResponseDetailStory> {
            override fun onResponse(
                call: Call<ResponseDetailStory>,
                response: Response<ResponseDetailStory>
            ) {
                if (response.isSuccessful) {
                    detailStoryResult.value = Result.Success(response.body()!!.story)
                } else {
                    detailStoryResult.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<ResponseDetailStory>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                detailStoryResult.value = Result.Error("No internet")
            }
        })
    }

    fun getStoriesData(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, userPreference)
            }
        ).liveData
    }

    suspend fun uploadStory(
        image: MultipartBody.Part,
        description: RequestBody,
        latitude: RequestBody? = null,
        longitude: RequestBody? = null
    ) {
        uploadResult.value = Result.Loading
        val token = runBlocking { userPreference.getSession().first().token }
        val bearerToken = "Bearer $token"
        val response = apiService.uploadStory(bearerToken, image, description, latitude, longitude)
        response.enqueue(object : Callback<ResponseUpload> {
            override fun onResponse(
                call: Call<ResponseUpload>,
                response: Response<ResponseUpload>
            ) {
                if (response.isSuccessful) {
                    uploadResult.value = Result.Success(response.body()!!)
                }else{
                    uploadResult.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<ResponseUpload>, t: Throwable) {
                uploadResult.value = Result.Error("No Internet")
                Log.e(TAG, "onFailure: ${t.message.toString()}")

            }
        })
    }

    fun getUploadResponse() = uploadResult

    fun getDetailStoryResponse() = detailStoryResult

    fun getStoriesWithLocation() = storiesWithLocationResult

    companion object {
        private const val TAG = "StoryRepository"
    }
}