package com.rohim.stroyapplast.ui

import android.Manifest
import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.rohim.stroyapplast.MainActivity
import com.rohim.stroyapplast.ViewModelFactory
import com.rohim.stroyapplast.data.service.ApiResult
import com.rohim.stroyapplast.databinding.ActivityAddStoryBinding
import com.rohim.stroyapplast.utils.createTempFile
import com.rohim.stroyapplast.utils.reduceFileImage
import com.rohim.stroyapplast.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String
    private lateinit var storyViewModel:StoryViewModel

    private var lat:Float?=null
    private var lon:Float?=null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setUpViewModel()
        playAnimation()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        binding.upload.setOnClickListener {
            storyViewModel.getUser().observe(this){

                val token = "Bearer " +it.token
                if (getFile != null) {
                    val file = reduceFileImage(getFile as File)

                    val description = "${binding.edtDescStory.text}".toRequestBody("text/plain".toMediaType())
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImageFile
                    )
                    storyViewModel.addStory(token, imageMultipart, description).observe(this){
                        when(it){
                            is ApiResult.Success -> {
                                showLoad(false)
                                showDialog(SUCCESS)

                            }
                            is ApiResult.Loading -> {
                                showDialog(ERROR)
                                showLoad(true)
                            }
                            is ApiResult.Error ->{
                                showDialog(ERROR)
                                showLoad(false)
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Masukan Image", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnGaleri.setOnClickListener { startGallery() }
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imPreviewStory, View.TRANSLATION_X, -27f, 27f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
    private  var isBackCamera:Boolean = false
    private  var isGalery:Boolean = false

    private var getFile: File? = null
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.imPreviewStory.setImageURI(selectedImg)

        }
    }
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.rohim.stroyapplast",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.imPreviewStory.setImageBitmap(result)
        }
    }
    private fun showDialog(mode:String) {
        val builder = AlertDialog.Builder(this)
        if (mode== SUCCESS){
            val title = SUCCESS
            val message = "Upload Story Berhasil"
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(android.R.string.ok) { _, _ ->
                val moveToMain = Intent(this, MainActivity::class.java)
                startActivity(moveToMain, ActivityOptions.makeSceneTransitionAnimation(this@AddStoryActivity).toBundle())
                finish()
            }
            builder.show()
        }else if (mode== ERROR){
            val title = ERROR
            val message = "Upload Gagal, coba lagi"
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(android.R.string.ok) { _, _ ->
                builder.show()
            }
        }

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
    private fun showLoad(isLoading: Boolean) {
        binding.progressBarAdd.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun setUpViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        storyViewModel = ViewModelProvider(this, factory)[StoryViewModel::class.java]
    }
    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        const val SUCCESS = "success"
        const val ERROR = "error"
    }
}