package com.example.weatherwise.favourite

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.SwipeableState
import androidx.wear.compose.material.swipeable
import com.example.weatherwise.data.model.FavouriteLocation
import com.example.weatherwise.data.repo.ResultState
import com.example.weatherwise.ui.theme.LightPurple
import kotlin.math.roundToInt

@Composable
fun FavouriteScreen(
    onNavigateToFavouriteMap: () -> Unit,
    viewModel: FavouriteViewModel,
    snackBarHostState: SnackbarHostState,
    onNavigateToHomeScreen: (lat: Double, lon: Double) -> Unit

) {
    val context = LocalContext.current

    
    LaunchedEffect(Unit) {
        Log.i("TAG", "FavouriteScreen:launchedEffect ")
        viewModel.getAllFavouriteLocations()
    }

  val favouriteLocations by  viewModel.favouriteLocations.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.message.collect { message ->
            // Only show Snackbar for deletion messages
            if (message.contains("deleted", ignoreCase = true)) {
                val result = snackBarHostState.showSnackbar(
                    message = message,
                    actionLabel = "Undo",
                    duration = SnackbarDuration.Long
                )
                if (result == SnackbarResult.ActionPerformed) {
                    Log.i("TAG", "FavouriteScreen: snackbar undo action $result")

                   viewModel.restoreFavouriteLocation()
                }
            }else{
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) { // Box allows absolute positioning

        when(favouriteLocations){
            is ResultState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is ResultState.Success -> {
                val locations = (favouriteLocations as ResultState.Success).data

                if (locations.isEmpty()) {
                    Text(text = "You haven't any favourite locations yet",modifier = Modifier.align(Alignment.Center),color = Color.White,
                        fontSize = 20.sp)
                }else{
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(items = locations,key = { it.cityName } ) { location ->
                            SwipeableCard(
                                location = location,
                                resetKey = location.hashCode(),
                                onDelete = {
                                    // Remove the card from the list
                                    viewModel.deleteFavouriteLocation(location)
                                },
                                onNavigateToHomeScreen = onNavigateToHomeScreen
                            )
                        }
                    }

                }

            }
            is ResultState.Failure -> {
                Text(text = (favouriteLocations as ResultState.Failure).message,modifier = Modifier.align(Alignment.Center),color = Color.White,
                    fontSize = 20.sp)
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
               // .padding(16.dp) // Adds padding to avoid touching screen edges
        ) {
            Icon(Icons.Default.AddLocation, contentDescription = "Favourite", Modifier.size(32.dp))
        }
    }
}


@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun SwipeableCard(
    resetKey: Int,
    onDelete: () -> Unit,
    location: FavouriteLocation,
    onNavigateToHomeScreen: (lat: Double, lon: Double) -> Unit
) {
    //val swipeableState = rememberSwipeableState(initialValue = 0)

    val swipeableState = remember(resetKey) {
        SwipeableState(initialValue = 0)
    }

    val maxSwipe = with(LocalDensity.current) { 100.dp.toPx() } // Swipe distance
    val anchors = mapOf(0f to 0, -maxSwipe to 1) // Swipe states



    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(bottom = 16.dp)
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
                .fillMaxSize()
                .clickable { //take the latitute and longitude and pass it to home screen
                    onNavigateToHomeScreen(location.latitude, location.longitude)
                },
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
                        text = location.cityName,
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
