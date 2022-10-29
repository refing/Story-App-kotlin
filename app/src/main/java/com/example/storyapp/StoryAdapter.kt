package com.example.storyapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.DetailActivity.Companion.EXTRA_STORY
import com.example.storyapp.api.Stories
import com.example.storyapp.databinding.ItemStoryBinding

class StoryAdapter :
    PagingDataAdapter<Stories, StoryAdapter.MyViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(holder.itemView.context, data)
        }
    }

    class MyViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, data: Stories) {
            Glide.with(itemView.context)
                .load(data.photoUrl)
                .into(binding.imgItemPhoto)
            binding.tvItemUsername.text = data.name
            itemView.setOnClickListener {
                Intent(context, DetailActivity::class.java).also { intent ->
                    intent.putExtra(EXTRA_STORY, data)
                    context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Stories>() {
            override fun areItemsTheSame(oldItem: Stories, newItem: Stories): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Stories, newItem: Stories): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}