package com.example.weatherwise

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherwise.Navigation.SetUpNavHost
import com.example.weatherwise.data.local.WeatherDatabase
import com.example.weatherwise.data.local.WeatherLocalDataSourceImpl
/*import com.example.weatherwise.data.local.WeatherDatabase
import com.example.weatherwise.data.local.WeatherLocalDataSourceImpl*/
import com.example.weatherwise.data.remote.RetrofitHelper
import com.example.weatherwise.data.remote.WeatherRemoteDataSourceImpl
import com.example.weatherwise.data.repo.WeatherRepositoryImpl
import com.example.weatherwise.favourite.map.MapViewModel
import com.example.weatherwise.home.viewModel.HomeViewModel
import com.example.weatherwise.utils.LocationService

const val REQUEST_LOCATION_CODE = 2005

class MainActivity : ComponentActivity() {
    private lateinit var locationService: LocationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = WeatherDatabase.getInstance(context = this)


        locationService = LocationService(this)

        setContent {
            val homeViewModel = ViewModelProvider(
                this,
                HomeViewModel.HomeFactory(
                    WeatherRepositoryImpl.getInstance(
                        WeatherRemoteDataSourceImpl(RetrofitHelper.service),
                        WeatherLocalDataSourceImpl(db.weatherDao())
                    )
                )
            ).get(HomeViewModel::class.java)

           /* val mapViewModel = ViewModelProvider(
                this,
                MapViewModel.MapScreenViewModelFactory(
                    WeatherRepositoryImpl.getInstance(
                        WeatherRemoteDataSourceImpl(RetrofitHelper.service),
                        WeatherLocalDataSourceImpl(db.weatherDao())
                    )
                )
            ).get(MapViewModel::class.java)*/

            SetUpNavHost(
                context = this,
                homeViewModel = homeViewModel,
                locationState = locationService.currentLocation
            )
        }
    }

    override fun onStart() {
        super.onStart()

        if (locationService.checkPermissions()) {
            if (locationService.isLocationEnabled()) {
                locationService.getFreshLocation()
            } else {
                locationService.enableLocationServices()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_LOCATION_CODE
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationService.getFreshLocation()
            }
        }
    }

}


/*class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val _locationState = MutableStateFlow<Location?>(null)
    val locationState = _locationState.asStateFlow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // val db = WeatherDatabase.getInstance(context = this)
       enableEdgeToEdge()
        setContent {

            val homeViewModel = ViewModelProvider(
                this,
                HomeViewModel.HomeFactory(
                    WeatherRepositoryImpl.getInstance(
                        WeatherRemoteDataSourceImpl(RetrofitHelper.service)
                        *//*,WeatherLocalDataSourceImpl(db.weatherDao())*//*
                    )
                )
            )
                .get(HomeViewModel::class.java)



            SetUpNavHost(this,homeViewModel,locationState)

        }
    }

    override fun onStart() {
        super.onStart()
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                getFreshLocation()
            } else {
                enableLocationServices()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_LOCATION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getFreshLocation()
            }
        }
    }

    fun checkPermissions(): Boolean {
        return checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getFreshLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(0).apply {
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            }.build(),
            object : LocationCallback() {
                override fun onLocationResult(location: LocationResult) {
                    super.onLocationResult(location)
                    _locationState.value = location.lastLocation
                   // _locationState.value = location.lastLocation!!
                }
            }, Looper.myLooper()
        )
    }

    fun enableLocationServices() {
        Toast.makeText(this, "Turn on Location", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }




}*/


/*  @Composable
  fun MainScreen() {

      Scaffold(
          bottomBar = {}) { innerPadding ->
          Box(
              modifier = Modifier
                  .fillMaxSize()
                  .padding(innerPadding)
                  .background(
                      brush = Brush.verticalGradient(
                          colorStops = arrayOf(
                              0f to Purple,      // First part (solid)
                              //  0.3f to Color(0xFF352163),    // Still Blue (No blend yet)
                              0.6f to PurpleBlue, // Mid transition (Blue → Magenta)
                              // 0.7f to Color(0xFF331972), // Still Magenta (No blend yet)
                              1f to PurplePink     // Last transition (Magenta → Cyan)
                          )
                      )
                  )
          ) {
              Image(
                  painter = painterResource(id = R.drawable.background),
                  contentDescription = null,
                  modifier = Modifier.fillMaxSize(),
                  contentScale = ContentScale.Fit
              )

              //Text(text = "Hello World!")
              HomeScreen()
          }
      }

  }*/