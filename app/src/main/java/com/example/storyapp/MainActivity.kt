package com.example.storyapp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }

    private lateinit var mSessionPreference: Preference
    private lateinit var sessionModel: SessionModel

    private var bearer: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        showRecyclerList(bearer)
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showRecyclerList(bearer:String) {

        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvStory.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvStory.layoutManager = LinearLayoutManager(this)
        }
        val listUserAdapter = StoryAdapter()
        binding.rvStory.adapter = listUserAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                listUserAdapter.retry()
            }
        )

//        mainViewModel.listStory.observe(this, {
//            listUserAdapter.submitData(lifecycle,it)
//        })

        mainViewModel.getStories(bearer).observe(this) { result ->
            listUserAdapter.submitData(lifecycle,result)
        }

    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
        const val EXTRA_NAME = "extra_name"
    }
}