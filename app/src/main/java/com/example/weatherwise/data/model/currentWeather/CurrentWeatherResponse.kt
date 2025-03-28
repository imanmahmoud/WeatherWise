package com.example.weatherwise.data.model.currentWeather

data class CurrentWeatherResponse(

    //5a46ee8289123314e05b723b9e36d002

   val coord: Coord= Coord(0.0,0.0),
    val weather: List<Weather>,
   // val base: String,
    val main: Main,
   // val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Int,
   // val sys: Sys,
   // val timezone: Int,
    val id: Int=0,
    val name: String="",
  //  val cod: Int,
    val formattedDt: String = ""
)
