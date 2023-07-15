package com.rohim.stroyapplast.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rohim.stroyapplast.data.model.UserModel
import com.rohim.stroyapplast.repositori.UserRepository

class MapsViewModel(private val repository: UserRepository): ViewModel() {

    fun getLocation(token: String) =
        repository.getLocation(token)
    fun getUserData(): LiveData<UserModel> {
        return repository.getUserData()
    }
}
