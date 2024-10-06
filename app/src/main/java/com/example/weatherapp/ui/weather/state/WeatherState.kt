package com.example.weatherapp.ui.weather.state

import com.example.weatherapp.data.db.WeatherData

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val weatherData: WeatherData) : WeatherState()
    data class Error(val message: String) : WeatherState()
}

sealed class StoreWeatherState {
    object Loading : StoreWeatherState()
    data class Success(val weatherData: List<WeatherData?>) : StoreWeatherState()
    data class Error(val message: String) : StoreWeatherState()
}