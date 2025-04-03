package com.example.weatherwise.utils


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocationService(private val context: Context) {
    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private val _locationState = MutableStateFlow<Location?>(null)
    val currentLocation = _locationState.asStateFlow()/*: StateFlow<Location?> = locationState*/

    @SuppressLint("MissingPermission")
    fun getFreshLocation() {
        fusedLocationProviderClient.requestLocationUpdates(LocationRequest.Builder(0).apply {
            setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        }.build(), object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
               // _locationState.value = locationResult.lastLocation
                if (locationResult.lastLocation != null && locationResult.lastLocation!!.latitude !=0.0 && locationResult.lastLocation!!.longitude !=0.0) {
                    _locationState.value = locationResult.lastLocation
                    fusedLocationProviderClient.removeLocationUpdates(this)
                }
            }
        }, Looper.myLooper()
        )
    }

    fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            context, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    fun enableLocationServices() {
        Toast.makeText(context, "Turn on Location", Toast.LENGTH_LONG).show()
        val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }

    fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}
