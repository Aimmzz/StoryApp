package com.rohim.stroyapplast

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rohim.stroyapplast.data.model.MapModel
import com.rohim.stroyapplast.databinding.ActivityMainBinding
import com.rohim.stroyapplast.ui.AddStoryActivity
import com.rohim.stroyapplast.ui.MapsActivity
import com.rohim.stroyapplast.ui.WelcomeActivity
import com.rohim.stroyapplast.ui.adapter.AllStoryAdapter
import com.rohim.stroyapplast.ui.adapter.LoadingStateAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private val listStory = ArrayList<MapModel>()
    private lateinit var storyAdapter: AllStoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setUpViewModel()

        mainViewModel.getUser().observe(this@MainActivity){ user ->
            if (user.isLogin){
                setStory()
                Toast.makeText(this,"anda sudah login", Toast.LENGTH_SHORT).show()
            }
            else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        with(binding.rvStory) {
            adapter = AllStoryAdapter()
            storyAdapter = AllStoryAdapter()
            setHasFixedSize(true)
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter{
                    storyAdapter.retry()
                })
        }
        binding.fabMapStory.setOnClickListener {
            val moveToMap = Intent(this, MapsActivity::class.java)
            startActivity(moveToMap)
        }
        binding.fabLogout.setOnClickListener {
            showDialog()
        }
        binding.fabAddStory.setOnClickListener {
            val moveToAdd = Intent(this, AddStoryActivity::class.java)
            startActivity(moveToAdd)
        }
    }
//    override fun onResume() {
        //        super.onResume()
//        getStory()
//        storyViewModel.getStoriesV2()
//    }
//    private fun getStory() {
//        val adapter = AllStoryAdapter(object:AllStoryAdapter.StoryOnClickListener{
//            override fun onClick(story: ListStoryItem) {
//                val intent = Intent(this@MainActivity, DetailStoryActivity::class.java)
//                intent.putExtra(STORY,story)
//                startActivity(intent,
//                    ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle())
//            }
//        })
//        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
//        binding.recyclerView.adapter = adapter.withLoadStateFooter(
//            footer = LoadingStateAdapter {
//                adapter.retry()
//            }
//        )
//        viewModelLocal.story.observe(this) {
//            adapter.submitData(lifecycle, it)
//        }
//        binding.recyclerView.setHasFixedSize(true)
//        binding.recyclerView.adapter = adapter
//        binding.recyclerView.layoutManager = layoutManager
//    }
    private fun setStory() {
        mainViewModel.getStories().observe(this@MainActivity) {
            storyAdapter.submitData(lifecycle, it)
            showLoad(false)
        }
    }
    private fun setUpViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }
    @SuppressLint("ResourceType")
    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.logout))
        builder.setMessage("Apakah yakin ingin logout?")
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            mainViewModel.logout()
            startActivity(Intent(this, WelcomeActivity::class.java))
            Toast.makeText(this,"Anda Berhasil Logout",Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
        }
        builder.show()
    }
    private fun showLoad(isLoading: Boolean) {
        binding.progressBarMain.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    companion object{
//        lateinit var prefLogin : Preference2
        const val LOC = "location"
        const val STORY = "story"
    }
}