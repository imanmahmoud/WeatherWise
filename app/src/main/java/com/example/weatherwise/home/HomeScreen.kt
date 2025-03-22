package com.example.weatherwise.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherwise.R
import com.example.weatherwise.ui.theme.LightPurple
@Preview
@Composable
fun HomeScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(start = 18.dp, end = 18.dp, top = 18.dp).verticalScroll(rememberScrollState()),
    ) {
        // Current Weather
        CurrentWeather()

        Spacer(modifier = Modifier.height(16.dp))

        // Weather Details Card
        WeatherDetailsCard()


        Spacer(modifier = Modifier.height(16.dp))

        // Today's Forecast
        Text("Today", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White,modifier = Modifier.align(Alignment.Start).padding(bottom = 10.dp))
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(getTodayForecast()) { forecast ->
                ForecastItem(forecast)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weekly Forecast
        Text("7-Day Forecast", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White,modifier = Modifier.align(Alignment.Start).padding(bottom = 10.dp))

        WeeklyForecastCard()
    }
}

@Composable
fun CurrentWeather(){
    Row {
        Text("Surat", fontSize = 32.sp, color = Color.White,modifier = Modifier.padding(end = 6.dp, bottom = 18.dp))
        Box(modifier = Modifier.padding(top = 8.dp)){
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "User Icon",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }


    }
    Text("Mostly Sunny", fontSize = 18.sp, color = Color.White)

    Spacer(modifier = Modifier.height(8.dp))

    Image(
        painter = painterResource(id = R.drawable.rainy_weather),
        contentDescription = "Weather Icon",
        modifier = Modifier.size(100.dp),
        contentScale = ContentScale.Crop
    )
    Row (modifier = Modifier.padding(bottom = 16.dp)){
        Text("23", fontSize = 60.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("°K", fontSize = 28.sp, fontWeight = FontWeight.Normal, color = Color.White)
    }
    Text("Feels like 23°", fontSize = 18.sp, color = Color.White, modifier = Modifier.padding(bottom = 8.dp))
    Text("Friday, 26 August 2022 | 10:00", fontSize = 18.sp, color = Color.White)

}

@Composable
fun WeatherDetailsCard() {
    Card(
        modifier = Modifier.wrapContentSize().padding(bottom = 12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LightPurple)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WeatherDetailItem("30%", "Clouds",R.drawable.cloud)
            WeatherDetailItem("20%", "Humidity",R.drawable.humidity)
            WeatherDetailItem("9 km/h", "Wind Speed",R.drawable.wind_speed)
            WeatherDetailItem("9 hpa", "Pressure",R.drawable.pressure)
        }
    }
}


@Composable
fun WeatherDetailItem(value: String, label: String,icon:Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "Weather Icon",
            modifier = Modifier.size(20.dp),
            contentScale = ContentScale.Crop
        )
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(label, fontSize = 14.sp, color = Color.White)
    }
}

@Composable
fun ForecastItem(forecast: Forecast) {

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
            Text(forecast.time, fontSize = 14.sp, color = Color.White)
            Image(
                painter = painterResource(id = forecast.icon), // Replace with actual resource
                contentDescription = "Weather Icon",
                modifier = Modifier.size(50.dp)
            )

            Text(forecast.temperature, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }

}

// Sample Data
data class Forecast(val time: String, val temperature: String, val icon: Int)

fun getTodayForecast(): List<Forecast> {
    return listOf(
        Forecast("10 AM", "23°", R.drawable.rainy_weather),
        Forecast("12 PM", "23°", R.drawable.ic_launcher_foreground),
        Forecast("1 PM", "23°", R.drawable.ic_launcher_foreground),
        Forecast("3 PM", "23°", R.drawable.ic_launcher_foreground),
        Forecast("5 PM", "23°", R.drawable.ic_launcher_foreground)
    )
}



@Composable
fun WeeklyForecastCard() {
    val weeklyForecast = listOf(
        ForecastItemData("Monday", R.drawable.rainy_weather, "Sunny", "+31°", "+51°"),
        ForecastItemData("Tuesday", R.drawable.rainy_weather, "Cloudy", "+31°", "+51°"),
        ForecastItemData("Wednesday", R.drawable.rainy_weather, "Thunder", "+31°", "+51°"),
        ForecastItemData("Thursday", R.drawable.rainy_weather, "Thunder", "+31°", "+51°"),
        ForecastItemData("Friday", R.drawable.rainy_weather, "Rain", "+31°", "+51°"),
        ForecastItemData("Saturday", R.drawable.rainy_weather, "Rain", "+31°", "+51°")
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LightPurple)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Column {
                weeklyForecast.forEach { forecast ->
                    ForecastRow(forecast)
                }
            }
        }
    }
}

@Composable
fun ForecastRow(forecast: ForecastItemData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(forecast.day, fontSize = 16.sp, color = Color.White, modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = forecast.icon),
            contentDescription = "Weather Icon",
            modifier = Modifier.size(30.dp)
        )

        Text(forecast.condition, fontSize = 14.sp, color = Color.White, modifier = Modifier.weight(1f))

        Text(forecast.minTemp, fontSize = 14.sp, color = Color.White)
        Text(forecast.maxTemp, fontSize = 14.sp, color = Color.White)
    }
}

// Data class for the forecast items
data class ForecastItemData(
    val day: String,
    val icon: Int,
    val condition: String,
    val minTemp: String,
    val maxTemp: String
)

