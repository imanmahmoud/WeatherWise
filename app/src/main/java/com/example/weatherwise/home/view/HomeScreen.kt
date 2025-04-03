package com.example.weatherwise.home.view

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
//import androidx.compose.material.Text
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherwise.R
import com.example.weatherwise.data.repo.ResultState
import com.example.weatherwise.data.sharedPreference.PreferenceHelper
import com.example.weatherwise.home.viewModel.HomeViewModel
import com.example.weatherwise.home.view.component.CurrentWeather
import com.example.weatherwise.home.view.component.DailyForecastCard
import com.example.weatherwise.home.view.component.HourlyForecastList
import com.example.weatherwise.utils.ApiConstants
import com.example.weatherwise.utils.getTempUnitDisplay
import com.example.weatherwise.utils.getWindSpeedUnitDisplay
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.StateFlow

//@Preview


@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    locationState: StateFlow<Location?>,
    lat: Double,
    lon: Double
) {
    val context = LocalContext.current
    val preferenceHelper = PreferenceHelper(context = context)

    val unit = preferenceHelper.getTempUnit()
    val language = preferenceHelper.getLanguage()
    val locationOption = preferenceHelper.getLocation()
    val pair = preferenceHelper.getMapGPSLocation()



    val tempUnit = getTempUnitDisplay(unit)
    val windSpeedUnit = getWindSpeedUnitDisplay(unit)


    var location: Location?
    val isFromFavourite: Boolean

    if (lat != 0.0 && lon != 0.0) {
        isFromFavourite = true
        location = Location("").apply {
            latitude = lat
            longitude = lon
        }
    } else {
        isFromFavourite = false
        if (locationOption == "Map") {
            location = Location("").apply {
                latitude = pair.first.toDouble()//mapLatitude.toDouble()
                longitude = pair.second.toDouble()//mapLongitude.toDouble()
            }

        } else {
            val gpsLocation by locationState.collectAsStateWithLifecycle()
            location = gpsLocation
            location?.let {
                preferenceHelper.saveMapGPSLocation(location.latitude, location.longitude)
            }

        }

    }

    LaunchedEffect(location) {
        location?.let {
            viewModel.fetchWeatherIfLocationChanged(it, isFromFavourite, unit, language)
        }
    }



    val currentWeatherState by viewModel.currentWeatherState.collectAsStateWithLifecycle()
    val hourlyForecastState by viewModel.hourlyForecastState.collectAsStateWithLifecycle()
    val dailyForecastState by viewModel.dailyForecastState.collectAsStateWithLifecycle()


    val isLoading = currentWeatherState is ResultState.Loading ||
            hourlyForecastState is ResultState.Loading ||
            dailyForecastState is ResultState.Loading


    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            location?.let {
                viewModel.fetchWeatherIfLocationChanged(it, isFromFavourite, unit, language)
            }
        }
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 18.dp, end = 18.dp, top = 18.dp)
                .verticalScroll(rememberScrollState()),
        ) {


            if (isLoading) {
                /* Box(
                 modifier = Modifier
                     .wrapContentSize(Alignment.Center)
                     *//*.background(Color.Gray)
                    .padding(8.dp)*//*
            ) {*/
                CircularProgressIndicator(color = Color.White)
                // }
            }


            val errorMessage = when {
                currentWeatherState is ResultState.Failure -> (currentWeatherState as ResultState.Failure).message
                hourlyForecastState is ResultState.Failure -> (hourlyForecastState as ResultState.Failure).message
                dailyForecastState is ResultState.Failure -> (dailyForecastState as ResultState.Failure).message
                else -> null
            }

            if (errorMessage != null) {

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = "User Icon",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = errorMessage,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }


            }

            if (currentWeatherState is ResultState.Success &&
                hourlyForecastState is ResultState.Success &&
                dailyForecastState is ResultState.Success
            ) {
                val currentWeather = (currentWeatherState as ResultState.Success).data
                val hourlyForecast = (hourlyForecastState as ResultState.Success).data
                val dailyForecast = (dailyForecastState as ResultState.Success).data

                CurrentWeather(currentWeather, tempUnit, windSpeedUnit)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    stringResource(R.string.today),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 10.dp)
                )

                HourlyForecastList(hourlyForecast, tempUnit)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    stringResource(R.string.daily_forecast),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 10.dp)
                )

                DailyForecastCard(dailyForecast, tempUnit)
            }
        }
    }
}


