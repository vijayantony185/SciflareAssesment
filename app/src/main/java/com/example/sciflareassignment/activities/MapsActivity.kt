package com.example.sciflareassignment.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sciflareassignment.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
       mMap = p0
        val latitude = intent?.getDoubleExtra("Latitude", 0.0) ?: 0.0
        val longitude = intent?.getDoubleExtra("Longitude", 0.0) ?: 0.0
        val markerPosition = LatLng(latitude, longitude)
        val markerTitle = "User Current Location"
        mMap.addMarker(MarkerOptions().position(markerPosition).title(markerTitle))

        // Move the camera to the marker and set the zoom level
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 12f))
    }
}