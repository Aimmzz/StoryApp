package com.rohim.stroyapplast.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rohim.stroyapplast.data.model.UserModel
import com.rohim.stroyapplast.repositori.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val repo: UserRepository) : ViewModel() {

    fun addStory(token: String, file: MultipartBody.Part, description: RequestBody) = repo.addStory(token, file, description)

    fun getUser(): LiveData<UserModel> {
        return repo.getUserData()
    }
}