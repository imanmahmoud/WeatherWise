package com.example.weatherwise.favourite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.weatherwise.ui.theme.LightPurple
import com.example.weatherwise.ui.theme.Purple

@Composable
fun FavouriteScreen(onNavigateToFavouriteMap: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) { // Box allows absolute positioning
        Column(modifier = Modifier.fillMaxSize()) {

        }

        FloatingActionButton(
            containerColor = Color(0xFF957DCD),
            contentColor = Color.White,
           // shape = CircleShape,
           // elevation = FloatingActionButtonDefaults.elevation(8.dp),
            onClick = { onNavigateToFavouriteMap() },
            modifier = Modifier
                .align(Alignment.BottomEnd)  // Aligns FAB to bottom end inside Box
                .padding(16.dp) // Adds padding to avoid touching screen edges
        ) {
            Icon(Icons.Default.AddLocation, contentDescription = "Favourite",Modifier.size(32.dp))
        }
    }
}
