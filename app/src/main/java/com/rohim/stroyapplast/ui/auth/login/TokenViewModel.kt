package com.rohim.stroyapplast.ui.auth.login
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.rohim.stroyapplast.data.response.LoginResult
//import com.rohim.stroyapplast.data.service.ApiConfig
//import com.rohim.stroyapplast.data.service.ApiResult
//import kotlinx.coroutines.launch
//
//class TokenViewModel: ViewModel() {
//    private val _responseLogin = MutableLiveData<ApiResult<LoginResult>>()
//    val responseLogin : LiveData<ApiResult<LoginResult>> = _responseLogin
//
//    fun login(email:String, password:String){
//        viewModelScope.launch {
//            _responseLogin.value = ApiResult.Loading
//            try {
//                val response = ApiConfig.getApiService().login2(email, password)
//                if(response.isSuccessful){
//                    val result = response.body()
//                    if (result != null) {
//                        _responseLogin.value = ApiResult.Success(result.loginResult)
//                    }
//                }else{
//                    _responseLogin.value = ApiResult.Error("Error: ${response.code()}")
//                }
//            }catch (e:Exception){
//                _responseLogin.value = ApiResult.Error(e.message.toString())
//            }
//        }
//
//    }
//}