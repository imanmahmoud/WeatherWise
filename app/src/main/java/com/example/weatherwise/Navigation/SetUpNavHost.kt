package com.example.weatherwise.Navigation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherwise.R
import com.example.weatherwise.alert.AlertScreen
import com.example.weatherwise.favourite.FavouriteScreen
import com.example.weatherwise.home.HomeScreen
import com.example.weatherwise.settings.SettingsScreen
import com.example.weatherwise.ui.theme.Purple
import com.example.weatherwise.ui.theme.PurpleBlue
import com.example.weatherwise.ui.theme.PurplePink

@Composable
fun SetUpNavHost(context: Context){
    var navController = rememberNavController()


    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
               /* .padding(innerPadding)*/
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

            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = ScreenRoutes.HomeRoute
            ){
                composable<ScreenRoutes.HomeRoute> {
                    HomeScreen()

                }
                composable<ScreenRoutes.FavouriteRoute> {

                    FavouriteScreen()

                }
                composable<ScreenRoutes.AlertRoute> {
                    AlertScreen()
                }
                composable<ScreenRoutes.SettingsRoute> {
                    SettingsScreen()
                }

            }


        }

    }










}



