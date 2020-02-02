package com.myapplicationusinggoogleplaces.view.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.facebook.FacebookSdk.getApplicationContext
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.myapplicationusinggoogleplaces.R
import com.myapplicationusinggoogleplaces.contants.PlacesConstant
import com.myapplicationusinggoogleplaces.model.Results

class RadarFragment : Fragment(), OnMapReadyCallback {

    private val locationPermissionRequestCode = 1234
    private val defaultZoom = 15f

    private lateinit var mMap: GoogleMap
    private var mLocationPermissionsGranted = false
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.radar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLocationPermission()
    }

    private fun getDeviceLocation() {
        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(getApplicationContext())
        try {
            if (mLocationPermissionsGranted) {
                val location: Task<Location>? = mFusedLocationProviderClient?.lastLocation
                location?.addOnCompleteListener(object : OnCompleteListener<Location> {
                    override fun onComplete(task: Task<Location>) {

                        if (task.isSuccessful) {
                            val currentLocation: Location = task.result as Location
                            PlacesConstant.latLng.value = LatLng(currentLocation.latitude, currentLocation.longitude)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLocation.latitude, currentLocation.longitude), defaultZoom))
                        } else {
                            Toast.makeText(getApplicationContext(), "unable to get current location", Toast.LENGTH_SHORT).show()
                        }

                    }

                })
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getLocationPermission() {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true
                initMap()
            } else {
                requestPermissions(permissions, locationPermissionRequestCode)
            }
        } else {
            requestPermissions(permissions, locationPermissionRequestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        mLocationPermissionsGranted = false
        when (requestCode) {
            locationPermissionRequestCode -> {
                if (grantResults.isNotEmpty()) {
                    var i = 0
                    while (i < grantResults.size) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false
                            return
                        }
                        i++
                    }
                    mLocationPermissionsGranted = true
                    initMap()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (mLocationPermissionsGranted) {
            getDeviceLocation()

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                return
            }

            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
            mMap.uiSettings.isZoomControlsEnabled = true

            PlacesConstant.results.observe(this, Observer<MutableList<Results>> {
                googleMap.clear()

                for (results in it) {
                    val markerOptions = MarkerOptions()
                    val googlePlace: Results = results
                    val lat: Double? = googlePlace.geometry?.location?.lat?.toDouble()
                    val lng: Double? = googlePlace.geometry?.location?.lng?.toDouble()
                    val placeName: String? = googlePlace.name
                    val latLng = LatLng(lat!!, lng!!)
                    markerOptions.position(latLng)
                    markerOptions.title(placeName)
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    // adicionando o marker no map
                    googleMap.addMarker(markerOptions)
                    // movendo camera
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(PlacesConstant.latLng.value, 12.0f))
                    googleMap.uiSettings.isCompassEnabled = true
                    googleMap.isMyLocationEnabled = true
                    googleMap.uiSettings.isMyLocationButtonEnabled = true
                    googleMap.uiSettings.isZoomControlsEnabled = true
                }

            })

        }

    }

}