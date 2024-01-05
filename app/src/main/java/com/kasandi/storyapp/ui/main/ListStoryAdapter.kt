package com.kasandi.storyapp.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.kasandi.storyapp.data.remote.response.Story
import com.kasandi.storyapp.databinding.ItemStoryBinding
import com.kasandi.storyapp.ui.detail.DetailStoryActivity

class ListStoryAdapter : PagingDataAdapter<Story, ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.tvNama.text = story.name
            binding.tvCaption.text = story.description
            Glide.with(binding.imgStory.context)
                .load(story.photoUrl)
                .transform(CenterCrop())
                .into(binding.imgStory)

            binding.cvStory.setOnClickListener {
                val intentDetail = Intent(binding.cvStory.context, DetailStoryActivity::class.java)
                intentDetail.putExtra(DetailStoryActivity.EXTRA_ID, story.id)
                binding.cvStory.context.startActivity(intentDetail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story!!)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }

}