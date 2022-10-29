package com.example.storyapp

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

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

    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        setStoryData()
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

    private fun setStoryData() {
        mainViewModel.getStoriesLocation(bearer).observe(this) { result ->
            result.onSuccess { response ->
                response.forEach{ story ->
                    val latLng = LatLng(story.lat, story.lon)
                    mMap.addMarker(MarkerOptions()
                        .position(latLng)
                        .title(story.name)
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
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    companion object {
        private val TAG = MapsActivity::class.java.simpleName
    }


}

