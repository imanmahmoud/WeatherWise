package com.example.weatherwise.home.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherwise.ui.theme.LightPurple

import androidx.compose.runtime.getValue
//import androidx.compose.material.Text
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherwise.data.repo.Response
import com.example.weatherwise.home.HomeViewModel
import com.example.weatherwise.home.view.component.CurrentWeather
import com.example.weatherwise.home.view.component.DailyForecastCard
import com.example.weatherwise.home.view.component.DailyForecastItemData
import com.example.weatherwise.home.view.component.HourlyForecastList
import com.example.weatherwise.home.view.component.getTodayForecast

//@Preview

const  val apiKey = "5a46ee8289123314e05b723b9e36d002"
@Composable
fun HomeScreen(viewModel: HomeViewModel) {


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(start = 18.dp, end = 18.dp, top = 18.dp).verticalScroll(rememberScrollState()),
    ) {


        viewModel.fetchWeather(30.033, 31.233, apiKey)
       // val products = viewModel.products.collectAsStateWithLifecycle()
        val uiState by viewModel.weatherState.collectAsStateWithLifecycle()



        when (uiState) {
            is Response.Loading -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize()) {
                    CircularProgressIndicator()
                }
            }

            is Response.Success -> {
                val currentWeather = uiState as Response.Success
                currentWeather.data?.let { CurrentWeather(it) }

            }

            is Response.Error -> {
                /*Toast.makeText(
                    this,
                    (uiState as Response.Error).errorMessage.message,
                    Toast.LENGTH_SHORT
                ).show()*/
            }


        }


        Spacer(modifier = Modifier.height(16.dp))

        // Today's Forecast
        Text("Today", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White,modifier = Modifier.align(Alignment.Start).padding(bottom = 10.dp))


        HourlyForecastList(getTodayForecast())

        Spacer(modifier = Modifier.height(16.dp))

        // Weekly Forecast
        Text("7-Day Forecast", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White,modifier = Modifier.align(Alignment.Start).padding(bottom = 10.dp))

       DailyForecastCard()
    }
}






















