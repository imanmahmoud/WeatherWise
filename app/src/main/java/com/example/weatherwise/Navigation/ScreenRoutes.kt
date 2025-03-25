package com.example.weatherwise.Navigation

import kotlinx.serialization.Serializable

sealed class ScreenRoutes {
    @Serializable
    object AlertRoute : ScreenRoutes()
    @Serializable
    object HomeRoute : ScreenRoutes()
    @Serializable
    object FavouriteRoute : ScreenRoutes()
    @Serializable
    object SettingsRoute : ScreenRoutes()
    @Serializable
    object MapRoute : ScreenRoutes()

}



