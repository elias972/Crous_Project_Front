// MapFragment.kt
package com.example.crous_project

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private val crousRepository = CrousRepository
    private lateinit var googleMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Use childFragmentManager to find the map fragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.frag_map_mapview) as? SupportMapFragment

        if (mapFragment != null) {
            mapFragment.getMapAsync(this) // Pass 'this' as the callback
        } else {
            // Handle the case where the map fragment is not found
            Log.e("MapFragment", "SupportMapFragment not found")
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        displayCrousOnMap()
    }

    fun updateMap() {
        if (::googleMap.isInitialized) {
            googleMap.clear()
            displayCrousOnMap()
        }
    }

    private fun displayCrousOnMap() {
            crousRepository.getAllCrous().forEach { crous ->
                crous.geolocalisation?.let { geolocalisation ->
                    val position = com.google.android.gms.maps.model.LatLng(
                        geolocalisation.latitude,
                        geolocalisation.longitude
                    )
                    googleMap.addMarker(
                        com.google.android.gms.maps.model.MarkerOptions()
                            .position(position)
                            .title(crous.nom)
                    )
                }
            }


        // Optionally, move the camera to a default position
        // For example, move the camera to the first Crous location
        crousRepository.getAllCrous().firstOrNull()?.geolocalisation?.let { geolocalisation ->
            val position = com.google.android.gms.maps.model.LatLng(
                geolocalisation.latitude,
                geolocalisation.longitude
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12f))
        }

    }
}
