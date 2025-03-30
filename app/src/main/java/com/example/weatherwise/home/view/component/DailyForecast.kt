package com.example.weatherwise.home.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse
import com.example.weatherwise.ui.theme.LightPurple
import com.example.weatherwise.ui.theme.LightPurpleO
import com.example.weatherwise.utils.WeatherIconHelper


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DailyForecastItem(dailyForecast: CurrentWeatherResponse) {
    val iconUrl = "https://openweathermap.org/img/wn/${dailyForecast.weather[0].icon}@2x.png"
    val image = WeatherIconHelper.getWeatherIcon(dailyForecast.weather[0].main)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(dailyForecast.formattedDt, fontSize = 16.sp, color = Color.White, modifier = Modifier.weight(1f))


       /* GlideImage(
            model = iconUrl,
            contentDescription = "Weather Icon",
            modifier = Modifier
                .size(38.dp)
                .padding(end = 8.dp)
        )*/
          Image(
              painter = painterResource(id = image),
              contentDescription = "Weather Icon",
              modifier = Modifier.size(38.dp).padding(end = 8.dp),
             // contentScale = ContentScale.Crop
          )

        Text(
            dailyForecast.weather[0].main,
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )

        Text("${dailyForecast.main.temp_max.toInt()}°c / ", fontSize = 14.sp, color = Color.White)
        Text("${dailyForecast.main.temp_min.toInt()}°c", fontSize = 14.sp, color = Color.White)
    }
}

@Composable
fun DailyForecastCard(dailyForecastList: List<CurrentWeatherResponse>) {

   /* val dailyForecastList = listOf(
        DailyForecastItemData("Monday", R.drawable.atmosphere_weather, "Sunny", "+31°", "+51°"),
        DailyForecastItemData("Tuesday", R.drawable.cloudy_weather, "Cloudy", "+31°", "+51°"),
        DailyForecastItemData("Wednesday", R.drawable.snow_weather, "Thunder", "+31°", "+51°"),
        DailyForecastItemData("Thursday", R.drawable.thunder_weather, "Thunder", "+31°", "+51°"),
        DailyForecastItemData("Friday", R.drawable.rain_weather, "Rain", "+31°", "+51°"),
        DailyForecastItemData("Saturday", R.drawable.sunny_weather, "Rain", "+31°", "+51°")
    )*/

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LightPurpleO)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Column {
                dailyForecastList.forEach { forecast ->
                    DailyForecastItem(forecast)
                }
            }
        }
    }
}

/*
// Data class for the forecast items
data class DailyForecastItemData(
    val day: String,
    val icon: Int,
    val condition: String,
    val minTemp: String,
    val maxTemp: String
)*/
