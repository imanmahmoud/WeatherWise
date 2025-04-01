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
import com.example.weatherwise.ui.theme.LightPurpleO
import com.example.weatherwise.utils.formatNumberBasedOnLanguage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HourlyForecastItem(hourlyForecast: CurrentWeatherResponse, tempUnit: String) {
    val iconUrl = "https://openweathermap.org/img/wn/${hourlyForecast.weather[0].icon}@2x.png"

    Card(
        modifier = Modifier.padding(end = 10.dp, bottom = 12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LightPurpleO)
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

            Text("${formatNumberBasedOnLanguage(hourlyForecast.main.temp.toInt().toString()) }${tempUnit}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }

}

@Composable
fun HourlyForecastList(hourlyForecastList: List<CurrentWeatherResponse>, tempUnit: String) {
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(hourlyForecastList) { forecast ->
            HourlyForecastItem(forecast, tempUnit)
        }
    }
}


