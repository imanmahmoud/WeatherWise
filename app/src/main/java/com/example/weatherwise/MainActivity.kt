package com.example.weatherwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.weatherwise.Navigation.SetUpNavHost
/*import com.example.weatherwise.data.local.WeatherDatabase
import com.example.weatherwise.data.local.WeatherLocalDataSourceImpl*/
import com.example.weatherwise.data.remote.RetrofitHelper
import com.example.weatherwise.data.remote.WeatherRemoteDataSourceImpl
import com.example.weatherwise.data.repo.WeatherRepositoryImpl
import com.example.weatherwise.home.viewModel.HomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // val db = WeatherDatabase.getInstance(context = this)
       enableEdgeToEdge()
        setContent {

            val homeViewModel = ViewModelProvider(
                this,
                HomeViewModel.HomeFactory(
                    WeatherRepositoryImpl.getInstance(
                        WeatherRemoteDataSourceImpl(RetrofitHelper.service)
                        /*,WeatherLocalDataSourceImpl(db.weatherDao())*/
                    )
                )
            )
                .get(HomeViewModel::class.java)



            SetUpNavHost(this,homeViewModel)

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


