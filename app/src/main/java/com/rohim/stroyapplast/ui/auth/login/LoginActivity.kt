package com.rohim.stroyapplast.ui.auth.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.rohim.stroyapplast.MainActivity
import com.rohim.stroyapplast.ViewModelFactory
import com.rohim.stroyapplast.data.model.UserModel
import com.rohim.stroyapplast.data.preference.Preference2
import com.rohim.stroyapplast.data.response.LoginResult
import com.rohim.stroyapplast.data.service.ApiResult
import com.rohim.stroyapplast.databinding.ActivityLoginBinding
import com.rohim.stroyapplast.ui.auth.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()
        playAnimation()

        binding.tvRegisterLogin.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmailLogin.text.toString()
            val password = binding.edtPassLogin.text.toString()
            if (email.isEmpty()) {
                binding.edtEmailLogin.error = "Input Email"
            } else if (password.isEmpty()) {
                binding.edtPassLogin.error = "Input Password"
            } else if(password.length<8){
                binding.edtPassLogin.requestFocus()
            }
            loginViewModel.userLogin(email, password).observe(this) {
                when (it) {
                    is ApiResult.Success -> {
                        showLoad(false)
                        dataStore
                        val response = it.data
                        saveUserData(
                            UserModel(
                                response.loginResult?.name.toString(),
                                response.loginResult?.token.toString(),
                                true
                            )
                        )
                        startActivity(Intent(this, MainActivity::class.java))
                        finishAffinity()
                    }
                    is ApiResult.Loading -> showLoad(true)
                    is ApiResult.Error -> {
                        showDialog(ERROR)
                        showLoad(false)
                    }
                }
            }
        }
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imDicodingLogin, View.TRANSLATION_Y, -40f, 40f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(1000)
        val tvEmail = ObjectAnimator.ofFloat(binding.tv1, View.ALPHA, 1f).setDuration(1000)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtEmailLogin, View.ALPHA, 1f).setDuration(1000)
        val tvPassword = ObjectAnimator.ofFloat(binding.tv2, View.ALPHA, 1f).setDuration(1000)
        val edtPassword = ObjectAnimator.ofFloat(binding.edtPassLogin, View.ALPHA, 1f).setDuration(1000)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(1000)
        val containerLogin = ObjectAnimator.ofFloat(binding.containerLogin, View.ALPHA, 1f).setDuration(1000)

        AnimatorSet().apply {
            playSequentially(title, tvEmail, edtEmail, tvPassword, edtPassword, btnLogin, containerLogin)
            start()
        }

    }
    private fun saveUserData(userData: UserModel) {
        loginViewModel.saveUserData(userData)
    }

    private fun moveToLogin() {
        Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    private fun showDialog(mode: String) {
        val builder = AlertDialog.Builder(this)
        if (mode == "error") {
            val title = ERROR
            val message = "Login gagal, silahkan coba lagi"
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(android.R.string.ok) { _, _ ->
            }
            builder.show()
        } else if (mode == "success") {
            val title = SUCCESS
            val message = "Login berhasil, lanjut login !"
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(android.R.string.ok) { _, _ ->
                moveToLogin()
            }
            builder.show()
        }
    }
    private fun saveToken(loginResult: LoginResult){
        val prefLogin = Preference2(this)
        prefLogin.setToken(loginResult.token)
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }
    private fun showLoad(isLoading: Boolean) {
        binding.progressBarLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    companion object {
        const val ERROR = "error"
        const val SUCCESS = "success"
    }
}