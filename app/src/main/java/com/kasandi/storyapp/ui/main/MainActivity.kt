package com.kasandi.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kasandi.storyapp.R
import com.kasandi.storyapp.databinding.ActivityMainBinding
import com.kasandi.storyapp.helper.ViewModelFactory
import com.kasandi.storyapp.ui.login.LoginActivity
import com.kasandi.storyapp.ui.map.MapsActivity
import com.kasandi.storyapp.ui.upload.UploadActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel =
            ViewModelProvider(this, ViewModelFactory.getInstance(this))[MainViewModel::class.java]
        val layoutManager = LinearLayoutManager(this)
        binding.rvMainStories.layoutManager = layoutManager

        setContentView(binding.root)
        initView()
        initAction()
    }

    private fun initAction() {
        binding.toolbarMain.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_main_logout -> {
                    isLoading(true)
                    viewModel.logout()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    true
                }
                R.id.item_main_maps -> {
                    isLoading(true)
                    startActivity(Intent(this, MapsActivity::class.java))
                    isLoading(false)
                    true
                }
                else -> false
            }
        }

        binding.fbMainAddStory.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

    private fun initView() {
        val adapter = ListStoryAdapter()
        binding.rvMainStories.adapter = adapter
        viewModel.stories.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbMain.visibility = View.VISIBLE
            binding.rvMainStories.visibility = View.INVISIBLE
        } else {
            binding.pbMain.visibility = View.INVISIBLE
            binding.rvMainStories.visibility = View.VISIBLE
        }
    }
}