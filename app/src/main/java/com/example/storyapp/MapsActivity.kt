package com.example.storyapp

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.Stories
import com.example.storyapp.api.StoriesResponse
import com.example.storyapp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var listStory: ArrayList<Story> = ArrayList()
    private var listlatlon: ArrayList<LatLng> = ArrayList()
    private var listlat: ArrayList<Double?> = ArrayList()
    private var listlon: ArrayList<Double?> = ArrayList()

    private lateinit var mSessionPreference: Preference
    private lateinit var sessionModel: SessionModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mSessionPreference = Preference(this)
        sessionModel = mSessionPreference.getSession()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

//        val dicodingSpace = LatLng(-6.8957643, 107.6338462)
//        mMap.addMarker(
//            MarkerOptions()
//                .position(dicodingSpace)
//                .title("Dicoding Space")
//                .snippet("Batik Kumeli No.50")
//        )
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 15f))

        mMap.uiSettings.isZoomControlsEnabled = true

        getStoriesLoc()
        setMapStyle()

    }
    private val boundsBuilder = LatLngBounds.Builder()
    private fun addManyMarker() {

        listStory.forEach { tourism ->
//            listlat.add(tourism.lat)
//            listlon.add(tourism.lon)
            val latLng = LatLng(tourism.lat!!, tourism.lon!!)
//            listlatlon.add(latLng)
            mMap.addMarker(MarkerOptions()
                .position(latLng)
                .title(tourism.name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)))
//                .icon(BitmapDescriptorFactory.fromPath(tourism.photoUrl!!)))
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
//        Log.e(MapsActivity.TAG, "tes liststory: ${listStory}")
//        Log.e(MapsActivity.TAG, "tes latlon: ${listlatlon}")
//        Log.e(MapsActivity.TAG, "tes lat: ${listlat}")
//        Log.e(MapsActivity.TAG, "tes lon: ${listlon}")

    }
    private fun getStoriesLoc() {
        showLoading(true)
        val client = ApiConfig.getApiService().getStoriesLoc("Bearer ${sessionModel.token}")
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
                    addManyMarker()

                } else {
                    Log.e(MapsActivity.TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                showLoading(false)
                Log.e(MapsActivity.TAG, "onFailure: ${t.message}")
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
                    stories.lat,
                    stories.lon,
                )
            )
        }
    }
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
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    companion object {
        private val TAG = MapsActivity::class.java.simpleName
    }


}