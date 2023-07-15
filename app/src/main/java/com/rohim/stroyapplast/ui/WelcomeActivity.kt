package com.rohim.stroyapplast.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rohim.stroyapplast.databinding.ActivityWelcomeBinding
import com.rohim.stroyapplast.ui.auth.login.LoginActivity
import com.rohim.stroyapplast.ui.auth.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAnimation()
        binding.btnToLogin.setOnClickListener {
            val moveToLogin = Intent(this@WelcomeActivity, LoginActivity::class.java)
            startActivity(moveToLogin)
        }
        binding.btnToRegister.setOnClickListener {
            val moveToRegister = Intent(this@WelcomeActivity, RegisterActivity::class.java)
            startActivity(moveToRegister)
        }
    }

    private fun setAnimation() {
        val tvAbout = ObjectAnimator.ofFloat(binding.tvAboutApp, View.ALPHA, 1f).setDuration(700)
        val btnToLogin = ObjectAnimator.ofFloat(binding.btnToLogin, View.ALPHA, 1f).setDuration(700)
        val btnToRegister = ObjectAnimator.ofFloat(binding.btnToRegister, View.ALPHA, 1f).setDuration(700)

        val container = AnimatorSet().apply {
            playTogether(btnToLogin, btnToRegister)
        }

        AnimatorSet().apply {
            playSequentially(tvAbout, container)
            start()
        }
    }
}