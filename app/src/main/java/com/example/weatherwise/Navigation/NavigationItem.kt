package com.example.weatherwise.Navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: ScreenRoutes
)

/*data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String // Change type from ScreenRoutes to String
)*/

