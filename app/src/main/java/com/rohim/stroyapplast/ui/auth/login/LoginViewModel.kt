package com.rohim.stroyapplast.ui.auth.login

import android.util.Log
import androidx.lifecycle.*
import com.rohim.stroyapplast.data.model.UserModel
import com.rohim.stroyapplast.data.preference.UserPreference
import com.rohim.stroyapplast.data.request.LoginRequest
import com.rohim.stroyapplast.data.response.LoginResponse
import com.rohim.stroyapplast.data.service.ApiResult
import com.rohim.stroyapplast.data.service.ApiService
import com.rohim.stroyapplast.repositori.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository): ViewModel() {

    fun userLogin(email: String, password: String) =
        repository.userLogin(email, password)

    fun saveUserData(userData: UserModel) {
        viewModelScope.launch {
            repository.saveUserData(userData)
        }
    }

    fun login() {
        viewModelScope.launch {
            repository.login()
        }
    }
}