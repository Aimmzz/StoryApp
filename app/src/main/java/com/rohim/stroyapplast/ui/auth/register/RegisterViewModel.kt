package com.rohim.stroyapplast.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohim.stroyapplast.data.model.UserModel
import com.rohim.stroyapplast.data.request.RegisterRequest
import com.rohim.stroyapplast.data.response.RegisterResponse
import com.rohim.stroyapplast.data.service.ApiConfig
import com.rohim.stroyapplast.data.service.ApiResult
import com.rohim.stroyapplast.repositori.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository): ViewModel() {
    private val _responeRegister = MutableLiveData<ApiResult<RegisterResponse>>()
    val responseRegister : LiveData<ApiResult<RegisterResponse>> = _responeRegister

    fun userRegister(name: String, email: String, password: String) =
        repository.userRegister(name, email, password)
}