package com.example.utampa.ui.theme.pages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.utampa.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Utampa)  // Ensure correct theme is applied
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val tampa = LatLng(27.9506, -82.4572)
        map.addMarker(MarkerOptions().position(tampa).title("University of Tampa"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(tampa, 15f))
    }
}
