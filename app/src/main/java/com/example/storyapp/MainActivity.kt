package com.example.storyapp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.api.*
import com.example.storyapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var listStory: ArrayList<Story> = ArrayList()

    private lateinit var mSessionPreference: Preference
    private lateinit var sessionModel: SessionModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mSessionPreference = Preference(this)
        sessionModel = mSessionPreference.getSession()


        binding.rvStory.setHasFixedSize(true)
        binding.tvTitle.text = resources.getString(R.string.welcome_s, sessionModel.name)
        binding.fabAddstory.setOnClickListener {
            val moveIntent = Intent(this@MainActivity, AddStoryActivity::class.java)
            moveIntent.putExtra(AddStoryActivity.EXTRA_TOKEN, sessionModel.token)
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
        getStories()
    }
    override fun onStart() {
        super.onStart()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mSessionPreference = Preference(this)
        sessionModel = mSessionPreference.getSession()

        binding.rvStory.setHasFixedSize(true)
        binding.tvTitle.text = resources.getString(R.string.welcome_s, sessionModel.name)
        binding.fabAddstory.setOnClickListener {
            val moveIntent = Intent(this@MainActivity, AddStoryActivity::class.java)
            moveIntent.putExtra(AddStoryActivity.EXTRA_TOKEN, sessionModel.token)
            startActivity(moveIntent)
        }
        binding.fabMaps.setOnClickListener {
            val moveIntent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(moveIntent)
        }
        binding.btnLogout.setOnClickListener{
            sessionModel = SessionModel("","")
            mSessionPreference.setSession(sessionModel)


            val i = Intent(this@MainActivity, LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i)
            this.finish()
        }
        getStories()
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun getStories() {
        showLoading(true)
        val client = ApiConfig.getApiService().getStories("Bearer ${sessionModel.token}")
        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    //populating rv
                    listStory.clear()
                    setStoryData(responseBody.listStory)
                    showRecyclerList()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
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
        val adapter = StoryAdapter(listStory)
        binding.rvStory.adapter = adapter
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