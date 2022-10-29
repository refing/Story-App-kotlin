package com.example.storyapp

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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

    private val bearer = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWlZc01tTUF3RWpPY01ieXAiLCJpYXQiOjE2NjQwNDk2MDh9.aXLES4iX00ROVG8HHoYKzsNhq0xUC6AN8n8Gpi2zuBo"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mSessionPreference = Preference(this)
        sessionModel = mSessionPreference.getSession()

        binding.rvStory.setHasFixedSize(true)
        binding.tvTitle.text = "Welcome, ${sessionModel.name}"
        Log.e(TAG, "tes main: ${sessionModel.name}")
        Log.e(TAG, "tes main: ${sessionModel.token}")
        binding.fabAddstory.setOnClickListener {
            val moveIntent = Intent(this@MainActivity, AddStoryActivity::class.java)
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
        val client = ApiConfig.getApiService().getStories(bearer)
        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    //populating rv
                    setRestaurantData(responseBody.listStory)
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
    private fun setRestaurantData(data: List<Stories>) {
        for (stories in data) {
            listStory.add(
                Story(
                    stories.id,
                    stories.name,
                    stories.description,
                    stories.photoUrl,
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