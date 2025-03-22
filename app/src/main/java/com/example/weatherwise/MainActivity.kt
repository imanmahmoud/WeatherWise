package com.example.weatherwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherwise.Navigation.SetUpNavHost
import com.example.weatherwise.home.HomeScreen
import com.example.weatherwise.ui.theme.Purple
import com.example.weatherwise.ui.theme.PurpleBlue
import com.example.weatherwise.ui.theme.PurplePink
import com.example.weatherwise.ui.theme.WeatherWiseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       enableEdgeToEdge()
        setContent {

            SetUpNavHost(this)

        }
    }

  /*  @Composable
    fun MainScreen() {

        Scaffold(
            bottomBar = {}) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to Purple,      // First part (solid)
                                //  0.3f to Color(0xFF352163),    // Still Blue (No blend yet)
                                0.6f to PurpleBlue, // Mid transition (Blue → Magenta)
                                // 0.7f to Color(0xFF331972), // Still Magenta (No blend yet)
                                1f to PurplePink     // Last transition (Magenta → Cyan)
                            )
                        )
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                //Text(text = "Hello World!")
                HomeScreen()
            }
        }

    }*/


}


