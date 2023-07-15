package com.rohim.stroyapplast.repositori

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.rohim.stroyapplast.data.model.UserModel
import com.rohim.stroyapplast.data.preference.UserPreference
import com.rohim.stroyapplast.data.request.LoginRequest
import com.rohim.stroyapplast.data.request.RegisterRequest
import com.rohim.stroyapplast.data.response.ListStoryItem
import com.rohim.stroyapplast.data.response.LoginResponse
import com.rohim.stroyapplast.data.response.RegisterResponse
import com.rohim.stroyapplast.data.response.StorysResponse
import com.rohim.stroyapplast.data.service.ApiResult
import com.rohim.stroyapplast.data.service.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository(private val pref: UserPreference, private val apiService: ApiService) {
    fun userRegister(name: String, email: String, password: String): LiveData<ApiResult<RegisterResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val response = apiService.register(
                RegisterRequest(name, email, password)
            )
            emit(ApiResult.Success(response))
        } catch (e: Exception) {
            Log.d("Signup", e.message.toString())
            emit(ApiResult.Error(e.message.toString()))
        }
    }
    fun userLogin(email: String, password: String): LiveData<ApiResult<LoginResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val response = apiService.login(LoginRequest(email, password))
            emit(ApiResult.Success(response))
        } catch (e: Exception) {
            Log.d("Login", e.message.toString())
            emit(ApiResult.Error(e.message.toString()))
        }
    }
    fun getStory(): LiveData<PagingData<ListStoryItem>>{
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                StoryPagging(apiService, pref)
            }
        ).liveData
    }
    fun getUserData(): LiveData<UserModel> {
        return pref.getUserData().asLiveData()
    }
    fun getLocation(token: String): LiveData<ApiResult<StorysResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val response = apiService.getLocation(token, 1)
            emit(ApiResult.Success(response))
        } catch (e: Exception) {
            Log.d("Signup", e.message.toString())
            emit(ApiResult.Error(e.message.toString()))
        }
    }
    fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<ApiResult<RegisterResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val response = apiService.addStory(token, file, description)
            emit(ApiResult.Success(response))
        } catch (e: Exception) {
            Log.d("Signup", e.message.toString())
            emit(ApiResult.Error(e.message.toString()))
        }
    }

    suspend fun saveUserData(userEntity: UserModel) {
        pref.saveUserData(userEntity)
    }

    suspend fun login() {
        pref.login()
    }

    suspend fun logout() {
        pref.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            preferences: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(preferences, apiService)
            }.also { instance = it }
    }
}