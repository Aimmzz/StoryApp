package com.rohim.stroyapplast.ui.auth.register

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
import com.rohim.stroyapplast.ViewModelFactory
import com.rohim.stroyapplast.data.service.ApiResult
import com.rohim.stroyapplast.databinding.ActivityRegisterBinding
import com.rohim.stroyapplast.ui.auth.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewModel()
        playAnimation()

        binding.tvLoginRegister.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnRegister.setOnClickListener {
            val name = binding.edtNameRegister.text.toString()
            val email = binding.edtEmailRegister.text.toString()
            val password = binding.edtPassRegister.text.toString()
            if (name.isEmpty()) {
                binding.edtNameRegister.error = "Input Nama"
            } else if (email.isEmpty()) {
                binding.edtEmailRegister.error = "Input Email"
            } else if (password.isEmpty()) {
                binding.edtPassRegister.error = "Input Password"
            } else if(password.length<8){
                binding.edtPassRegister.requestFocus()
            }
            registerViewModel.userRegister(name, email, password).observe(this) {
                when (it) {
                    is ApiResult.Success -> {
                        showLoad(false)
                        showDialog(SUCCESS)
                        startActivity(Intent(this, LoginActivity::class.java))
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
        ObjectAnimator.ofFloat(binding.imDicodingRegister, View.TRANSLATION_Y, -40f, 40f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val title = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(1000)
        val tvName = ObjectAnimator.ofFloat(binding.tv5, View.ALPHA, 1f).setDuration(1000)
        val edtName = ObjectAnimator.ofFloat(binding.edtNameRegister, View.ALPHA, 1f).setDuration(1000)
        val tvEmail = ObjectAnimator.ofFloat(binding.tv3, View.ALPHA, 1f).setDuration(1000)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtEmailRegister, View.ALPHA, 1f).setDuration(1000)
        val tvPassword = ObjectAnimator.ofFloat(binding.tv4, View.ALPHA, 1f).setDuration(1000)
        val edtPassword = ObjectAnimator.ofFloat(binding.edtPassRegister, View.ALPHA, 1f).setDuration(1000)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(1000)
        val containerRegister = ObjectAnimator.ofFloat(binding.containerRegister, View.ALPHA, 1f).setDuration(1000)

        AnimatorSet().apply {
            playSequentially(title, tvName, edtName, tvEmail, edtEmail, tvPassword, edtPassword, btnRegister, containerRegister)
            start()
        }

    }
    private fun moveToLogin() {
        Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    private fun showDialog(mode: String) {
        val builder = AlertDialog.Builder(this)
        if (mode == ERROR) {
            val title = ERROR
            val message = "Register gagal, silahkan coba lagi"
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(android.R.string.ok) { _, _ ->
            }
            builder.show()
        } else if (mode == SUCCESS) {
            val title = SUCCESS
            val message = "Register berhasil, lanjut login !"
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(android.R.string.ok) { _, _ ->
                moveToLogin()
            }
            builder.show()
        }
    }
    private fun setUpViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        registerViewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]
    }
    private fun showLoad(isLoading: Boolean) {
        binding.progressBarRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    companion object {
        const val ERROR = "error"
        const val SUCCESS = "success"
    }
}