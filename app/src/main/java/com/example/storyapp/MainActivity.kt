package com.example.storyapp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.api.*
import com.example.storyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var listStory: ArrayList<Story> = ArrayList()

    private lateinit var mSessionPreference: Preference
    private lateinit var sessionModel: SessionModel

    private var bearer: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MainViewModel::class.java)



        mainViewModel.listStory.observe(this, { liststory ->
            setStoryData(liststory)
        })

        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        mSessionPreference = Preference(this)
        sessionModel = mSessionPreference.getSession()

        bearer = sessionModel.token ?: ""

        binding.rvStory.setHasFixedSize(true)
        binding.tvTitle.text = resources.getString(R.string.welcome_s, sessionModel.name)
        binding.fabAddstory.setOnClickListener {
            val moveIntent = Intent(this@MainActivity, AddStoryActivity::class.java)
            moveIntent.putExtra(AddStoryActivity.EXTRA_TOKEN, bearer)
            startActivity(moveIntent)
        }
        binding.fabMaps.setOnClickListener {
            val moveIntentMaps = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(moveIntentMaps)
        }
        binding.btnLogout.setOnClickListener{
            sessionModel = SessionModel("","")
            mSessionPreference.setSession(sessionModel)

            val i = Intent(this@MainActivity, LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i)
            this.finish()
        }
        mainViewModel.getStories(bearer)
    }
    private fun setStoryData(data: List<Stories>) {
        for (stories in data) {
            listStory.add(
                Story(
                    stories.id,
                    stories.name,
                    stories.description,
                    stories.photoUrl,
                    stories.lon,
                    stories.lat
                )
            )
        }
        showRecyclerList()
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showRecyclerList() {

        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvStory.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvStory.layoutManager = LinearLayoutManager(this)
        }
        val listUserAdapter = StoryAdapter(listStory)
        binding.rvStory.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Story) {
                val moveWithObjectIntent = Intent(this@MainActivity, DetailActivity::class.java)
                moveWithObjectIntent.putExtra(DetailActivity.EXTRA_STORY, data)
                startActivity(moveWithObjectIntent)
            }
        })
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val EXTRA_TOKEN = "extra_token"
        const val EXTRA_NAME = "extra_name"
    }
}