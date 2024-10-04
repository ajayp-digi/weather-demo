package com.example.weatherapp.data.model

data class WeatherResponse(
    val name: String,
    val sys: Sys,
    val main: Main,
    val coord: Coord
)

data class Sys(val country: String, val sunrise: Long, val sunset: Long)
data class Main(val temp: Double)
data class Coord(val lat: Double, val lon: Double)