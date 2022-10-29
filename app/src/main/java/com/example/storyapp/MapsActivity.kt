package com.example.storyapp

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.api.Stories
import com.example.storyapp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*



class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var listStory: ArrayList<Story> = ArrayList()

    private lateinit var mSessionPreference: Preference
    private lateinit var sessionModel: SessionModel

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }
    private var bearer: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mSessionPreference = Preference(this)
        sessionModel = mSessionPreference.getSession()

        bearer = sessionModel.token ?: ""

        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mainViewModel.getStoriesLoc(bearer)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        mainViewModel.listStoryLoc.observe(this, { liststory ->
            setStoryData(liststory)
        })

        setMapStyle()

    }
    private val boundsBuilder = LatLngBounds.Builder()

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }
    data class Story(
        var name: String? = null,
        var lat: Double? = null,
        var lon: Double? = null)
    private fun setStoryData(data: List<Stories>) {
        for (stories in data) {
            listStory.add(
                Story(
                    stories.name,
                    stories.lat,
                    stories.lon,
                )
            )
        }
        listStory.forEach { tourism ->
            val latLng = LatLng(tourism.lat!!, tourism.lon!!)
            mMap.addMarker(MarkerOptions()
                .position(latLng)
                .title(tourism.name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)))
            boundsBuilder.include(latLng)
        }
        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )

    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    companion object {
        private val TAG = MapsActivity::class.java.simpleName
    }


}

