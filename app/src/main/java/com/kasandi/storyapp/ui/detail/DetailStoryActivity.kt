package com.kasandi.storyapp.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.kasandi.storyapp.data.Result
import com.kasandi.storyapp.data.remote.response.Story
import com.kasandi.storyapp.databinding.ActivityDetailStoryBinding
import com.kasandi.storyapp.helper.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var viewModel: DetailStoryViewModel
    private lateinit var id: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[DetailStoryViewModel::class.java]
        id = intent.getStringExtra(EXTRA_ID).toString()
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        viewModel.getDetailStoryResponse().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        isLoading(true)
                    }

                    is Result.Success -> {
                        setData(result.data)
                        isLoading(false)
                    }

                    is Result.Error -> {
                        Toast.makeText(
                            this,
                            "Failed load data: ${result.error}!",
                            Toast.LENGTH_SHORT
                        ).show()
                        isLoading(false)
                    }
                }
            }

        }

        viewModel.setDetailStory(id)
    }

    private fun setData(story: Story) {
        binding.tvDetailUsername.text = story.name
        binding.tvDetailCaption.text = story.description
        Glide.with(this)
            .load(story.photoUrl)
            .transform(FitCenter())
            .into(binding.imgDetail)
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbDetail.visibility = View.VISIBLE
            binding.imgDetail.visibility = View.INVISIBLE
            binding.tvDetailUsername.visibility = View.INVISIBLE
            binding.tvDetailCaption.visibility = View.INVISIBLE
        } else {
            binding.pbDetail.visibility = View.INVISIBLE
            binding.imgDetail.visibility = View.VISIBLE
            binding.tvDetailUsername.visibility = View.VISIBLE
            binding.tvDetailCaption.visibility = View.VISIBLE
        }
    }

    companion object {
        const val EXTRA_ID = "id"
    }
}