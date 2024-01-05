package com.kasandi.storyapp.ui.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.kasandi.storyapp.data.Result
import com.kasandi.storyapp.databinding.ActivityUploadBinding
import com.kasandi.storyapp.helper.ViewModelFactory
import com.kasandi.storyapp.helper.reduceFileImage
import com.kasandi.storyapp.helper.uriToFile
import com.kasandi.storyapp.ui.camera.CameraActivity
import com.kasandi.storyapp.ui.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.kasandi.storyapp.ui.main.MainActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var viewModel: UploadViewModel
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        viewModel =
            ViewModelProvider(this, ViewModelFactory.getInstance(this))[UploadViewModel::class.java]
        setContentView(binding.root)
        initAction()
    }

    private fun initAction() {

        binding.btUploadGallery.setOnClickListener {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btUploadCamera.setOnClickListener {
            if (!allPermissionsGranted()) {
                requestPermissionLauncher.launch(REQUIRED_PERMISSION)
            }
            val intent = Intent(this, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }

        binding.btUploadSubmit.setOnClickListener {
            viewModel.getUploadStoryResponse().observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            isLoading(true)
                        }

                        is Result.Success -> {
                            Toast.makeText(this, "Upload success!", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@UploadActivity, MainActivity::class.java))
                            finish()
                        }

                        is Result.Error -> {
                            if (binding.etUploadDescription.text.isNullOrBlank()) {
                                Toast.makeText(
                                    this,
                                    "Failed upload: Description must filled!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }else{
                                Toast.makeText(
                                    this,
                                    "Failed upload: ${result.error}!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            isLoading(false)
                        }
                    }
                }
            }

            uploadImage()
        }
    }


    private fun setImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imgUploadPreview.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.etUploadDescription.text.toString().trim()

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody =
                MultipartBody.Part.createFormData("photo", imageFile.name, requestImageFile)

            viewModel.uploadStory(multipartBody, requestBody)
        }
    }

    private val launcherIntentCameraX =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == CAMERAX_RESULT) {
                currentImageUri =
                    it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
                setImage()
            }
        }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        this,
        REQUIRED_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                currentImageUri = uri
                setImage()
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbUpload.visibility = View.VISIBLE
            binding.imgUploadPreview.visibility = View.INVISIBLE
            binding.btUploadCamera.visibility = View.INVISIBLE
            binding.btUploadGallery.visibility = View.INVISIBLE
            binding.etlUploadDescription.visibility = View.INVISIBLE
            binding.etUploadDescription.visibility = View.INVISIBLE
            binding.btUploadSubmit.visibility = View.INVISIBLE
        } else {
            binding.pbUpload.visibility = View.INVISIBLE
            binding.imgUploadPreview.visibility = View.VISIBLE
            binding.btUploadCamera.visibility = View.VISIBLE
            binding.btUploadGallery.visibility = View.VISIBLE
            binding.etlUploadDescription.visibility = View.VISIBLE
            binding.etUploadDescription.visibility = View.VISIBLE
            binding.btUploadSubmit.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}