package com.example.weatherwise.home.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.LocationOn
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
//import androidx.compose.material.Text
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherwise.data.repo.Result
import com.example.weatherwise.home.viewModel.HomeViewModel
import com.example.weatherwise.home.view.component.CurrentWeather
import com.example.weatherwise.home.view.component.DailyForecastCard
import com.example.weatherwise.home.view.component.HourlyForecastList

//@Preview

const val apiKey = "5a46ee8289123314e05b723b9e36d002"


@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 18.dp, end = 18.dp, top = 18.dp)
            .verticalScroll(rememberScrollState()),
    ) {

        LaunchedEffect(Unit) {
            viewModel.fetchCurrentWeather(31.265, 29.989, apiKey)
            viewModel.fetchWeatherForecast(31.265, 29.989, apiKey)
        }

        val currentWeatherState by viewModel.currentWeatherState.collectAsStateWithLifecycle()
        val hourlyForecastState by viewModel.hourlyForecastState.collectAsStateWithLifecycle()
        val dailyForecastState by viewModel.dailyForecastState.collectAsStateWithLifecycle()


        val isLoading = currentWeatherState is Result.Loading ||
                hourlyForecastState is Result.Loading ||
                dailyForecastState is Result.Loading

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
            currentWeatherState is Result.Failure -> (currentWeatherState as Result.Failure).message
            hourlyForecastState is Result.Failure -> (hourlyForecastState as Result.Failure).message
            dailyForecastState is Result.Failure -> (dailyForecastState as Result.Failure).message
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

        if (currentWeatherState is Result.Success &&
            hourlyForecastState is Result.Success &&
            dailyForecastState is Result.Success
        ) {
            val currentWeather = (currentWeatherState as Result.Success).data
            val hourlyForecast = (hourlyForecastState as Result.Success).data
            val dailyForecast = (dailyForecastState as Result.Success).data

            CurrentWeather(currentWeather)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Today",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 10.dp)
            )

            HourlyForecastList(hourlyForecast)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "5-Day Forecast",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 10.dp)
            )

            DailyForecastCard(dailyForecast)
        }
    }
}


