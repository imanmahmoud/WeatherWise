package com.example.weatherwise.Navigation

import kotlinx.serialization.Serializable

sealed class ScreenRoutes {
    @Serializable
    object AlertRoute : ScreenRoutes()
    @Serializable
    data class HomeRoute(val lat: Double=0.0, val lon: Double=0.0) : ScreenRoutes()
    @Serializable
    object FavouriteRoute : ScreenRoutes()
    @Serializable
    object SettingsRoute : ScreenRoutes()
    @Serializable
    data class MapRoute(val isFromFavourite: Boolean) : ScreenRoutes()

}



