package com.example.weatherwise.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherwise.ui.theme.Purple




import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherwise.R
import com.example.weatherwise.utils.getTempUnitDisplay
import com.example.weatherwise.utils.mapDisplayToLanguage
import com.example.weatherwise.utils.mapDisplayToLocation
import com.example.weatherwise.utils.mapDisplayToTempUnit
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {
    val context = LocalContext.current

    val tempUnit by settingsViewModel.tempUnit.collectAsStateWithLifecycle()
    val windSpeedUnit by settingsViewModel.windSpeedUnit.collectAsStateWithLifecycle()
    val language by settingsViewModel.language.collectAsStateWithLifecycle()
    val location by settingsViewModel.location.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Language Card
        SettingsCard(
            title = stringResource(R.string.language),
            icon = Icons.Default.Translate,
            options = listOf(
                stringResource(R.string.arabic),
                stringResource(R.string.english),
                stringResource(R.string.default_lang)
            ),
            selectedOption = when (language) {
                "ar" -> stringResource(R.string.arabic)
                "en" -> stringResource(R.string.english)
                else -> stringResource(R.string.default_lang) // If empty or unknown, show "Default"
            },
            onOptionSelected = { newLanguage ->
                val mappedLanguage = mapDisplayToLanguage(newLanguage)
                coroutineScope.launch {
                    settingsViewModel.updateLanguage(mappedLanguage,context)
                }
            }
        )

        // Temperature Unit Card
        SettingsCard(
            title = stringResource(R.string.temp_unit),
            icon = Icons.Default.AcUnit,
            options = listOf(
                stringResource(R.string.celsius),
                stringResource(R.string.kelvin),
                stringResource(R.string.fahrenheit)
            ),
            selectedOption = getTempUnitDisplay(tempUnit),//if(getTempUnitDisplay(tempUnit)=="°C") stringResource(R.string.celsius) else if(getTempUnitDisplay(tempUnit)=="°K") stringResource(R.string.kelvin) else stringResource(R.string.fahrenheit),
            onOptionSelected = { newTempUnit ->
                val mappedUnit = mapDisplayToTempUnit(newTempUnit)
                coroutineScope.launch {
                    settingsViewModel.updateTempUnit(mappedUnit)
                }
            }
        )

        // Location Card
        SettingsCard(
            title = stringResource(R.string.location),
            icon = Icons.Default.LocationOn,
            options = listOf(
                stringResource(R.string.gps),
                stringResource(R.string.map)
            ),
            selectedOption = when(location){
                "Map" -> stringResource(R.string.map)
                else -> stringResource(R.string.gps)
            } ,//if(location=="Map") stringResource(R.string.map) else stringResource(R.string.gps),
            onOptionSelected = { newLocation ->
                val mappedLocation = mapDisplayToLocation(newLocation)
                coroutineScope.launch {
                    settingsViewModel.updateLocation(mappedLocation)
                }
            }
        )

        // Wind Speed Unit Card
        SettingsCard(
            title = stringResource(R.string.wind_speed_unit),
            icon = Icons.Default.Air,
            options = listOf(
                stringResource(R.string.meter_sec),
                stringResource(R.string.miles_hour)
            ),
            selectedOption = when(windSpeedUnit){
                "meter/sec" -> stringResource(R.string.meter_sec)
                else -> stringResource(R.string.miles_hour)
            },//if(windSpeedUnit=="meter/sec") stringResource(R.string.meter_sec) else stringResource(R.string.miles_hour),
            onOptionSelected = { }
        )
    }
}


@Composable
fun SettingsCard(
    title: String,
    icon: ImageVector,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Purple),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = title, fontSize = 18.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = (option == selectedOption),
                            onClick = { onOptionSelected(option) },
                            colors = RadioButtonDefaults.colors(selectedColor = Color.Magenta)
                        )
                        Text(text = option, color = Color.White, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}








/*
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    var selectedLanguage by remember { mutableStateOf("English") }
    var selectedTempUnit by remember { mutableStateOf("Celsius°C") }
    var selectedLocation by remember { mutableStateOf("Map") }
    var selectedWindSpeed by remember { mutableStateOf("meter/sec") }

    Column(
        modifier = Modifier
            .fillMaxSize()
           // .background(Color(0xFF1A1A2E)) // Dark background
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SettingsCard(
            title = "Language",
            icon = Icons.Default.Translate,// R.drawable.ic_language, // Replace with actual icon
            options = listOf("Arabic", "English", "Default"),
            selectedOption = selectedLanguage,
            onOptionSelected = { selectedLanguage = it }
        )

        SettingsCard(
            title = "Temp Unit",
            icon = Icons.Default.AcUnit,//R.drawable.ic_temperature, // Replace with actual icon
            options = */
/*listOf("Celsius", "Kelvin", "Fahrenheit"),*//*
listOf("°C", "°K", "°F"),
            selectedOption = selectedTempUnit,
            onOptionSelected = { selectedTempUnit = it }
        )

        SettingsCard(
            title = "Location",
            icon = Icons.Default.LocationOn,//R.drawable.ic_location, // Replace with actual icon
            options = listOf("Gps", "Map"),
            selectedOption = selectedLocation,
            onOptionSelected = { selectedLocation = it }
        )

        SettingsCard(
            title = "Wind Speed Unit",
            icon = Icons.Default.Air,//R.drawable.ic_wind_speed, // Replace with actual icon
            options = listOf("meter/sec", "miles/hour"),
            selectedOption = selectedWindSpeed,
            onOptionSelected = { selectedWindSpeed = it }
        )
    }
}

@Composable
fun SettingsCard(
    title: String,
    icon: ImageVector,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Purple*/
/*Color(0xFF22223B)*//*
), // Dark purple
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                   //
                    //painter = painterResource(id = icon),
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = title, fontSize = 18.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            modifier = Modifier.padding( 0.dp),
                            selected = (option == selectedOption),
                            onClick = { onOptionSelected(option) },
                            colors = RadioButtonDefaults.colors(selectedColor = Color.Magenta)
                        )
                       // Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option, color = Color.White, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}*/
