package com.example.weatherwise.favourite

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.weatherwise.ui.theme.LightPurple
import com.example.weatherwise.ui.theme.LightPurpleO
import com.example.weatherwise.ui.theme.Purple
import kotlin.math.roundToInt

@Composable
fun FavouriteScreen(onNavigateToFavouriteMap: () -> Unit) {
    var locations by remember { mutableStateOf(listOf("Egypt, New Valley Governorate")) }
    Box(modifier = Modifier.fillMaxSize()) { // Box allows absolute positioning
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(locations) { card ->
                SwipeableCard(
                    title = "Egypt",

                    onDelete = {
                        // Remove the card from the list
                        locations = locations.filter { it != card }
                        // Toast.makeText(LocalContext.current, "${card.first} deleted", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        FloatingActionButton(
            containerColor = LightPurple,
            contentColor = Color.White,
            // shape = CircleShape,
            // elevation = FloatingActionButtonDefaults.elevation(8.dp),
            onClick = { onNavigateToFavouriteMap() },
            modifier = Modifier
                .align(Alignment.BottomEnd)  // Aligns FAB to bottom end inside Box
                .padding(16.dp) // Adds padding to avoid touching screen edges
        ) {
            Icon(Icons.Default.AddLocation, contentDescription = "Favourite", Modifier.size(32.dp))
        }
    }
}


@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun SwipeableCard(
    title: String,
    onDelete: () -> Unit
) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val maxSwipe = with(LocalDensity.current) { 100.dp.toPx() } // Swipe distance
    val anchors = mapOf(0f to 0, -maxSwipe to 1) // Swipe states

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
    ) {
        // Background delete button with matching rounded corners
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(12.dp), // Same corner shape as the foreground card
            colors = CardDefaults.cardColors(containerColor = Color.Red) // Set the background color to red
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Location",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 24.dp)
                        .clickable { onDelete() } // Trigger delete action
                        .size(32.dp) // Size of the icon
                )
            }
        }

        // Foreground content (Card)
        Card(

            modifier = Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .fillMaxSize(),
            shape = RoundedCornerShape(12.dp), // Same corner radius
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = LightPurple) // Purple background for the card
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                       // style = MaterialTheme.typography.titleMedium,
                       fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Details",
                    tint = Color.White
                )
            }
            /*Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }*/
        }
    }
}


/*@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun SwipeableCard(
    //modifier: Modifier = Modifier,
    title: String,
   // subtitle: String,
    onDelete: () -> Unit
) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val maxSwipe = with(LocalDensity.current) { 200.dp.toPx() } // Swipe distance
    val anchors = mapOf(0f to 0, -maxSwipe to 1) // Swipe states

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal

            )
    ) {
        // Background delete button

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red)
                .border(1.dp, Color.Red, RoundedCornerShape(12.dp))
                .align(Alignment.CenterEnd)
        ) {
            Icon(
                //modifier = Modifier.padding(end = 24.dp),
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Location",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 24.dp)
                    .clickable { onDelete() } // Trigger the delete action
                    .size(32.dp) // Size of the delete icon
            )
        }

        // Foreground content (Card)
        Card(
            modifier = Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .fillMaxSize(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = LightPurple)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
               // Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }
    }
}*/
