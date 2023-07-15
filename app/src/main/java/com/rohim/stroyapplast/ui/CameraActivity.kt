package com.rohim.stroyapplast.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rohim.stroyapplast.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}