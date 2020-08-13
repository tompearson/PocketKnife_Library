package com.example.library_map

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private lateinit var mMap: GoogleMap
    lateinit var latLng: LatLng
    private lateinit var placesClient: PlacesClient
    //    private lateinit var placeId: String
    private var place: Place? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    //    private FusedLocationProviderClient mFusedLocationProviderClient
    private var mLastKnownLocation: Location? = null
    private var locationCallback: LocationCallback? = null
    private var mUiSettings: UiSettings? = null

    internal var places_api_key = BuildConfig.places_api_key


    private val DEFAULT_ZOOM = 15f

    val placeFields = mutableListOf(Place.Field.ID, Place.Field.NAME)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            this
        )

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, places_api_key)
        }
//        Places.initialize(getApplicationContext(), places_api_key)

        placesClient = Places.createClient(this)


        // Define a Place ID.
        // Portland ChIJJ3SpfQsLlVQRkYXR9ua5Nhw
        // EiRCZWVybWFuIENyZWVrIFJvYWQsIFNlYXNpZGUsIE9SLCBVU0EiLiosChQKEgl_mKMLLaKUVBFKxCZWtvTvGxIUChIJZ2YOtixhk1QRIv0bVPQMZI0 Beerman Creek Road Seaside
//        placeId = places_api_key


        // Use fields to define the data types to return.
        val placeFields = Collections.singletonList(Place.Field.NAME)

// Use the builder to create a FindCurrentPlaceRequest.
//        val placerequest = FindCurrentPlaceRequest.newInstance(placeFields)    never used


        val fetchPlaceRequest = FetchPlaceRequest.builder(places_api_key, placeFields)
            .build()

        // Add a listener to handle the response. TBD This is getting an exception
        placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener { response ->
            place = response.place
        }.addOnFailureListener { exception ->
            if (exception is ApiException) {
//                val statusCode =
                //        exception.statusCode // TDO Status{statusCode=This IP, site or mobile application is not authorized to use this API key. Request received from IP address 50.53.164.11, with empty referer, resolution=null}
                // Handle error with given status code.
            }
        }


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mUiSettings = mMap.getUiSettings()
        getDeviceLocation()
        updateLocationUI()

    }

    @SuppressLint("MissingPermission")
    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "onMyLocationButtonClick:\n" + "what is this?", Toast.LENGTH_LONG)
            .show();
        getDeviceLocation()
        updateLocationUI()

        return false
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "onMyLocationClick:\n" + "what is this?", Toast.LENGTH_LONG).show();

    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        mMap.setMyLocationEnabled(true)
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)

//        MAP_TYPE_NORMAL = 1;
//        MAP_TYPE_SATELLITE = 2;
//        MAP_TYPE_TERRAIN = 3;
//        MAP_TYPE_HYBRID = 4;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE)
        mMap.getUiSettings()?.apply {
            isZoomControlsEnabled = false
            isCompassEnabled = true
            isMyLocationButtonEnabled = true
            isMapToolbarEnabled = true
            isCompassEnabled = true
            isZoomControlsEnabled = true
            isRotateGesturesEnabled = true
            isTiltGesturesEnabled = true
        }
    }

    //    https://github.com/Manuaravind1989/KotlinCurrentLatLng/blob/master/app/src/main/java/com/mobiledev/locationprovider/MainActivity.kt
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE])
    @SuppressLint("MissingPermission")
    fun getDeviceLocation() {
        mFusedLocationProviderClient!!.lastLocation
            .addOnCompleteListener(OnCompleteListener<Location> { task ->
                if (task.isSuccessful && task.result != null) {
                    mLastKnownLocation = task.result
                    if (mLastKnownLocation != null) {
//                        mMap.moveCamera(
//                            CameraUpdateFactory.newLatLngZoom(
//                                LatLng(
//                                    mLastKnownLocation!!.getLatitude(),
//                                    mLastKnownLocation!!.getLongitude()
//                                ), DEFAULT_ZOOM
//                            )
//                        )
                        assignToMap()
                    } else {// lastLocation is null
                        val locationRequest = LocationRequest.create()
                        locationRequest.interval = 10000
                        locationRequest.fastestInterval = 5000
                        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        locationCallback = object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult?) {
                                super.onLocationResult(locationResult)
                                if (locationResult == null) {
                                    return
                                }
                                mLastKnownLocation = locationResult.lastLocation
                                assignToMap()
//                                mMap.moveCamera(
//                                    CameraUpdateFactory.newLatLngZoom(
//                                        LatLng(
//                                            mLastKnownLocation!!.getLatitude(),
//                                            mLastKnownLocation!!.getLongitude()
//                                        ), DEFAULT_ZOOM
//                                    )
//                                )
                                mFusedLocationProviderClient?.removeLocationUpdates(locationCallback)
                            }
                        }
                        mFusedLocationProviderClient?.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            null
                        )

                    }
                } else {
                    Toast.makeText(this, "unable to get last location", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun assignToMap() {
        mMap.clear()
        latLng = LatLng(mLastKnownLocation!!.getLatitude(), mLastKnownLocation!!.getLongitude())

        val options = MarkerOptions()
            .position(latLng)
            .title("My Location")
        mMap.apply {
            addMarker(options)
            moveCamera(CameraUpdateFactory.newLatLng(latLng))
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }
}