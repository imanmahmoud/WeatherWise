package com.example.weatherwise.home.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse
import com.example.weatherwise.ui.theme.LightPurple

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HourlyForecastItem(hourlyForecast: CurrentWeatherResponse) {
    val iconUrl = "https://openweathermap.org/img/wn/${hourlyForecast.weather[0].icon}@2x.png"

    Card(
        modifier = Modifier.padding(end = 10.dp, bottom = 12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LightPurple)
    ){
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)


        ) {
            Text(hourlyForecast.formattedDt, fontSize = 14.sp, color = Color.White)



            // Load the image using Glide
            GlideImage(
                model = iconUrl, contentDescription = "Weather Icon", modifier = Modifier.size(50.dp)
            )
          /*  Image(
                painter = painterResource(id = forecast.icon),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(50.dp)
            )*/

            Text("${hourlyForecast.main.temp.toInt()}°c", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }

}

@Composable
fun HourlyForecastList(hourlyForecastList: List<CurrentWeatherResponse>) {
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(hourlyForecastList) { forecast ->
            HourlyForecastItem(forecast)
        }
    }
}

/*
// Sample Data
data class HourlyForecast(val time: String, val temperature: String, val icon: Int)

fun getTodayForecast(): List<HourlyForecast> {
    return listOf(
        HourlyForecast("10 AM", "23°", R.drawable.rainy_weather),
        HourlyForecast("12 PM", "23°", R.drawable.ic_launcher_foreground),
        HourlyForecast("1 PM", "23°", R.drawable.ic_launcher_foreground),
        HourlyForecast("3 PM", "23°", R.drawable.ic_launcher_foreground),
        HourlyForecast("5 PM", "23°", R.drawable.ic_launcher_foreground)
    )
}*/
