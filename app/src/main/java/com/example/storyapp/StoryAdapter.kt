package com.example.storyapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ItemStoryBinding

class StoryAdapter(private val listStory: ArrayList<Story>) : RecyclerView.Adapter<StoryAdapter.ListViewHolder>()  {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (id, name, description,  photoUrl) = listStory[position]
        Glide.with(holder.itemView.context)
            .load(photoUrl)
            .into(holder.binding.imgItemPhoto)
        holder.binding.tvItemUsername.text = name
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listStory[holder.adapterPosition]) }

    }

    override fun getItemCount(): Int = listStory.size
    interface OnItemClickCallback {
        fun onItemClicked(data: Story)
    }
}