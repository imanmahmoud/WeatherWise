package com.example.weatherwise.alert.view

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.SwipeableState
import androidx.wear.compose.material.swipeable
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weatherwise.alert.MyWorker
import com.example.weatherwise.data.local.WeatherDatabase
import com.example.weatherwise.data.local.WeatherLocalDataSourceImpl
import com.example.weatherwise.data.remote.RetrofitHelper
import com.example.weatherwise.data.remote.WeatherRemoteDataSourceImpl
import com.example.weatherwise.data.repo.WeatherRepositoryImpl
/*import com.example.weatherwizard.MyWorker
import com.example.weatherwizard.Network.RemoteDataSource
import com.example.weatherwizard.Network.RetrofitHelper
import com.example.weatherwizard.Repository*/
import com.example.weatherwizard.alert.model.AlertModel
import com.example.weatherwise.alert.viewModel.AlertViewModel
import com.example.weatherwise.ui.theme.LightPurple
import com.example.weatherwise.ui.theme.LightPurpleO
/*import com.example.weatherwizard.data.database.AppDb
import com.example.weatherwizard.data.database.LocalDataSource*/
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt


@Preview
@Composable
fun AlertScreen() {
    val context = LocalContext.current
    val db = WeatherDatabase.getInstance(context = context)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            101
        )
    }

    val permissionStatus = ActivityCompat.checkSelfPermission(
        context, Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED

    Log.i("TAG", "Notification Permission Granted: $permissionStatus")

    val AlertsFactory = AlertViewModel.AlertViewModelFactory(
        repository = WeatherRepositoryImpl.getInstance(
            WeatherRemoteDataSourceImpl(RetrofitHelper.service), WeatherLocalDataSourceImpl(
                db.weatherDao()
            )

        )
    )
    val viewModel: AlertViewModel = viewModel(factory = AlertsFactory)
    val showBottomSheet = remember { mutableStateOf(false) } // Fixed mutable state
    LaunchedEffect(Unit) {
        viewModel.getAlerts()
    }
    val alerts = viewModel.alerts.collectAsStateWithLifecycle()


    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(items = alerts.value,key = { it.hashCode() } ) { alert ->
                AlertCard(
                   alert=alert,
                    resetKey = alert.hashCode(),
                    action = { viewModel.deleteAlert(it) },
                )
            }
        }

        FloatingActionButton(
            containerColor = LightPurple,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = { showBottomSheet.value = true },
        ) {
            Icon(
                imageVector = Icons.Default.NotificationAdd,
                contentDescription = "Open Bottom Sheet",

            )
        }

        if (showBottomSheet.value) {
            BottomSheetContent(showBottomSheet, viewModel)

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(showBottomSheet: MutableState<Boolean>, viewModel: AlertViewModel) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance() // Get today's date


    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var selectedDate = remember { mutableStateOf("Alert Date") } // Store selected date
    val verified = remember { mutableStateOf(true) }
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            selectedDate.value =
                "$selectedDay/${selectedMonth + 1}/$selectedYear" // Format selected date
        },
        year,
        month,
        day
    ).apply {
        datePicker.minDate = calendar.timeInMillis // Set minimum date to today
    }
    // Get current time

    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    var selectedTime = remember { mutableStateOf("Alert Time") } // Store selected time

    val timePickerDialog = TimePickerDialog(
        context,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            selectedTime.value =
                String.format("%02d:%02d", selectedHour, selectedMinute) // Format time as HH:mm
        },
        hour,
        minute,
        true // 24-hour format
    )
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { showBottomSheet.value = false },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Date", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Button(
                onClick = { datePickerDialog.show() }, colors = ButtonDefaults.buttonColors(
                    Color.White
                ), modifier = Modifier.padding(vertical = 16.dp), shape = RoundedCornerShape(8.dp)
            ) {
                Text(selectedDate.value, color = MaterialTheme.colorScheme.primary)
            }
            Text("Time", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Button(
                onClick = { timePickerDialog.show() }, colors = ButtonDefaults.buttonColors(
                    Color.White
                ), modifier = Modifier.padding(vertical = 16.dp), shape = RoundedCornerShape(8.dp)
            ) {
                Text(selectedTime.value, color = MaterialTheme.colorScheme.primary)
            }
            if (verified.value == false) {
                Text("Incorrect time", color = Color.Red, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {

                        val delay = viewModel.calculateDelay(selectedDate.value, selectedTime.value)
                        if (delay > 0) {
                            var request = OneTimeWorkRequestBuilder<MyWorker>().setInitialDelay(
                                delay,
                                TimeUnit.MILLISECONDS
                            )
                                .build()
                            WorkManager.getInstance(context).enqueue(request)
                            verified.value = true
                            viewModel.insertAlert(
                                AlertModel(
                                    selectedDate.value,
                                    selectedTime.value
                                )
                            )
                            showBottomSheet.value = false
                        } else {
                            verified.value = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.primary
                    ), shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Save", fontSize = 18.sp)
                }
                Button(
                    onClick = { showBottomSheet.value = false },
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.primary
                    ), shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cancel", fontSize = 18.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun AlertCard(alert: AlertModel, action: (AlertModel) -> Unit, resetKey: Int) {

    val swipeableState = remember(resetKey) {
        SwipeableState(initialValue = 0)
    }

    val maxSwipe = with(LocalDensity.current) { 400.dp.toPx() } // Swipe distance
    val anchors = mapOf(0f to 0, -maxSwipe to 1) // Swipe states

    if (swipeableState.currentValue == 1) {
       action.invoke(alert)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(bottom = 16.dp)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
    ) {
    Card(

        modifier = Modifier
            .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
            .fillMaxSize(),
            // .border(1.dp, Color.White, RoundedCornerShape(12.dp))

        // shape = RoundedCornerShape(12.dp), // Same corner radius
        // elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = LightPurpleO) // Purple background for the card
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(/*vertical = 20.dp,*/ horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Date : ${alert.date}", color = Color.White, fontSize = 18.sp,

                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Time : ${alert.time}", color = Color.White, fontSize = 18.sp,

                   // modifier = Modifier.padding(top = 8.dp)
                )
            }
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Favorite",
                tint = Color.White, modifier = Modifier.padding(top = 8.dp)
            )


            /*  Row(Modifier.padding(top = 8.dp)) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Favorite",
                tint = Color.White, modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Date : ${alert.date}", color = Color.White, fontSize = 18.sp,

                modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
            )
            Text(
                text = "Time : ${alert.time}", color = Color.White, fontSize = 18.sp,

                modifier = Modifier.padding(top = 8.dp)
            )
        }



        Button(
            onClick = { action.invoke(alert) },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(bottom = 8.dp, end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Favorite",
                tint = Color.White, modifier = Modifier.padding(top = 0.dp)
            )

        }*/
        }
    }
        }
}