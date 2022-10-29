package com.example.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.databinding.ActivityMainBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<Story>(EXTRA_STORY) as Story


        Glide.with(this)
            .load(story.photoUrl)
            .into(binding.imgPhoto)
        binding.tvNama.text = story.name.toString()
        binding.tvDeskripsi.text = story.description.toString()
    }
    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}