package com.example.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.api.Stories
import com.example.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<Stories>(EXTRA_STORY) as Stories

        Glide.with(this)
            .load(story.photoUrl)
            .into(binding.imgPhoto)
        binding.tvNama.text = story.name
        binding.tvDeskripsi.text = story.description
    }
    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}