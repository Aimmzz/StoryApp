package com.rohim.stroyapplast.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rohim.stroyapplast.data.response.ListStoryItem
import com.rohim.stroyapplast.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val detail = intent.getParcelableExtra<ListStoryItem>(DETAIL)

        Glide.with(this)
            .load(detail?.photoUrl)
            .into(binding.imItemStoryDetail)
        binding.tvNameUserDetail.text = detail?.name
        binding.tvDescDetail.text = detail?.description
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    companion object{
        const val DETAIL = "detail"
    }
}