package com.example.weatherwise.alert

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherwise.MainActivity
import com.example.weatherwise.R
import com.example.weatherwise.data.local.WeatherDatabase
import com.example.weatherwise.data.local.WeatherLocalDataSourceImpl
import com.example.weatherwise.data.remote.RetrofitHelper
import com.example.weatherwise.data.remote.WeatherRemoteDataSourceImpl
import com.example.weatherwise.data.repo.WeatherRepositoryImpl
import com.example.weatherwise.data.sharedPreference.PreferenceHelper
import com.example.weatherwise.utils.ApiConstants
import kotlinx.coroutines.flow.first

class MyWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    val context = context
    companion object {
        var mediaPlayer: MediaPlayer? = null
    }
    private val repo = WeatherRepositoryImpl.getInstance(
        WeatherRemoteDataSourceImpl(RetrofitHelper.service),
        WeatherLocalDataSourceImpl(
            WeatherDatabase.getInstance(context = context).weatherDao()
        )
    )

    override suspend fun doWork(): Result {
        return try {
            val preferenceHelper = PreferenceHelper(context = context)
            val pair = preferenceHelper.getMapGPSLocation()
            val language = preferenceHelper.getLanguage()
            val unit = preferenceHelper.getTempUnit()
            Log.i("TAG", "doWork: $pair")
            val weatherResponse = repo.getCurrentWeather(pair.first.toDouble(), pair.second.toDouble(),ApiConstants.WEATHER_API_KEY, language, unit).first()
            Log.i("TAG", "showNotification: $weatherResponse")

            // Extract data (Customize based on your API response)
            val temperature = weatherResponse.main?.temp ?: 0.0
            val description = weatherResponse.weather?.firstOrNull()?.description ?: "No description"
            Log.i("TAG", "showNotification: work")
            // Show notification
            showNotification("Weather Update", "Temp: $temperature°C, $description")

            Result.success()
        } catch (e: Exception) {
            Log.i("TAG", "showNotification: error ${e.message}")
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "weather_alert_channel"

        // Create Notification Channel (Required for Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Weather Alerts",
                NotificationManager.IMPORTANCE_HIGH

            ).apply {
                description = "Weather alert notifications"
                setSound(null, null)
            }

            val notificationManager = applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

        }
        mediaPlayer?.release() // Release any existing player
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.ringtone)
        mediaPlayer?.isLooping = true // Loop the sound
        mediaPlayer?.start()
        val dismissIntent = Intent(applicationContext, NotificationDismissReceiver::class.java)
        val dismissPendingIntent = PendingIntent.getBroadcast(
            applicationContext, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        /*val mainIntent = Intent(applicationContext, NotificationNavigateReceiver::class.java)
        val mainPendingIntent = PendingIntent.getBroadcast(
            applicationContext, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )*/



        // ✅ PendingIntent to OPEN `MainActivity` and STOP MediaPlayer
        val mainIntent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("STOP_ALARM", true) // ✅ Pass extra to stop alarm
        }

        val mainPendingIntent = PendingIntent.getActivity(
            applicationContext, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )



        // Build and Show Notification
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.cloud)  // Replace with your actual drawable icon
            .setContentTitle(title)
            .setContentText(message)
            .addAction(R.drawable.ic_launcher_foreground, "Dismiss", dismissPendingIntent)
            .addAction(R.drawable.ic_launcher_foreground, "View Details", mainPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            Log.i("TAG", "Notification permission zz")
            return
        }
        NotificationManagerCompat.from(applicationContext).notify(1, notification)
    }
}