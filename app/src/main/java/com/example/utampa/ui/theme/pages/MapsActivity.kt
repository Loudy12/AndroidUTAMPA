package com.example.utampa.ui.theme.pages

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.example.utampa.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class Building(
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val icon: String
)

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var searchView: SearchView  // Use SearchView from androidx.appcompat.widget
    private lateinit var buildings: List<Building>
    private val markerMap = mutableMapOf<String, Marker>() // Store markers for search

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Utampa)  // Ensure correct theme is applied
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Initialize Toolbar and set back button
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)  // Set the toolbar as the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // Enable back button

        // Initialize SearchView
        searchView = findViewById(R.id.searchView)

        // Initialize map fragment and setup map
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Set up the search view
        setupSearchView()

        // Load building data asynchronously using Coroutine
        coroutineScope.launch(Dispatchers.IO) {
            buildings = loadBuildingsFromAssets(this@MapsActivity)
            withContext(Dispatchers.Main) {
                // After loading the buildings, add markers to map
                addMarkersToMap()
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Move camera to University of Tampa
        val tampa = LatLng(27.9506, -82.4572)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(tampa, 15f))
    }

    private fun addMarkersToMap() {
        for (building in buildings) {
            val location = LatLng(building.latitude, building.longitude)
            val marker = map.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(building.name)
                    .snippet(building.description)
                    .icon(getIconForBuilding(building.icon))
            )
            // Store the marker with its name for later search
            marker?.let { markerMap[building.name.lowercase()] = it }
        }
    }

    private fun setupSearchView() {
        // Listen to search query text changes
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchBuilding(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun searchBuilding(query: String) {
        val lowerQuery = query.lowercase()
        val result = buildings.find { it.name.lowercase().contains(lowerQuery) }

        if (result != null) {
            // Get the location of the building
            val location = LatLng(result.latitude, result.longitude)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 18f)) // Zoom into the building location
            markerMap[result.name.lowercase()]?.showInfoWindow() // Show info window on marker
        } else {
            searchView.setQuery("", false)
            searchView.clearFocus()
        }
    }

    private suspend fun loadBuildingsFromAssets(context: Context): List<Building> {
        return withContext(Dispatchers.IO) {
            try {
                val jsonString = context.assets.open("campusData.json")
                    .bufferedReader().use { it.readText() }

                val listType = object : TypeToken<List<Building>>() {}.type
                Gson().fromJson(jsonString, listType)
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList() // Return an empty list if something goes wrong
            }
        }
    }

    private fun getIconForBuilding(iconName: String): BitmapDescriptor {
        return when (iconName) {
            "house" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
            "briefcase" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            "figure.walk" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            "bed.double.fill" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
            "heart" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)
            "bolt" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
            "person.2" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)
            else -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE) // Default color
        }
    }

    // Handle back button click from the action bar
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Go back to the previous activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Cancel the coroutine job when activity is destroyed
    override fun onDestroy() {
        super.onDestroy()
        job.cancel() // Cancel coroutine job to avoid memory leaks
    }
}
