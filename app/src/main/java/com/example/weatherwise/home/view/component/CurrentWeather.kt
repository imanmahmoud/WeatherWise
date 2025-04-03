package com.example.weatherwise.home.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherwise.R
import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse
import com.example.weatherwise.ui.theme.LightPurpleO
import com.example.weatherwise.utils.WeatherIconHelper
import com.example.weatherwise.utils.formatNumberBasedOnLanguage


@Composable
fun CurrentWeather(
    currentWeather: CurrentWeatherResponse,
    tempUnit: String,
    windSpeedUnit: String
) {

    val image = WeatherIconHelper.getWeatherIcon(currentWeather.weather[0].main)

    Row {
        Text(
            currentWeather.name,
            fontSize = 32.sp,
            color = Color.White,
            modifier = Modifier.padding(end = 6.dp, bottom = 18.dp)
        )
        Box(modifier = Modifier.padding(top = 8.dp)) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "User Icon",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }


    }
    Text(currentWeather.weather[0].description, fontSize = 24.sp, color = Color.White)

    Spacer(modifier = Modifier.height(8.dp))

    Image(
        painter = painterResource(id = image),
        contentDescription = "Weather Icon",
        modifier = Modifier
            .size(100.dp)
            .padding(vertical = 8.dp),
        // contentScale = ContentScale.Fit
    )
    Row(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            formatNumberBasedOnLanguage(currentWeather.main.temp.toInt().toString()),//"${currentWeather.main.temp.toInt()}",
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(tempUnit, fontSize = 32.sp, fontWeight = FontWeight.Normal, color = Color.White)
    }

    Text(
        stringResource(R.string.feels_like, formatNumberBasedOnLanguage(currentWeather.main.feels_like.toInt().toString()) , tempUnit),
        fontSize = 18.sp,
        color = Color.White,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    Text(
        currentWeather.formattedDt,
        fontSize = 18.sp,
        color = Color.White,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    WeatherDetailsCard(currentWeather, windSpeedUnit)


}

@Composable
fun WeatherDetailsCard(currentWeather: CurrentWeatherResponse, windSpeedUnit: String) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LightPurpleO)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WeatherDetailItem(
                "${currentWeather.clouds.all}%",
                stringResource(R.string.clouds), R.drawable.cloud
            )
            WeatherDetailItem(
                "${currentWeather.main.humidity}%",
                stringResource(R.string.humidity), R.drawable.humidity
            )
            WeatherDetailItem(
                "${currentWeather.wind.speed} ${windSpeedUnit}",
                stringResource(R.string.wind_speed), R.drawable.wind_speed
            )
            WeatherDetailItem(
                stringResource(R.string.hpa, currentWeather.main.pressure),
                stringResource(R.string.pressure),
                R.drawable.pressure
            )
        }
    }
}

@Composable
fun WeatherDetailItem(value: String, label: String, icon: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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

