package com.example.weatherapp.ui.storedWeather

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.Date

@Composable
fun StoredWeather(viewModel: WeatherViewModel = hiltViewModel()) {
    val storedWeatherState by viewModel.storedWeatherState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getStoredWeather()
    }

    storedWeatherState?.let { weather ->
        Column {
            Text("Stored City: ${weather.city}")
            Text("Stored Country: ${weather.country}")
            Text("Stored Temperature: ${weather.temperature}°C")
            Text("Stored Sunrise: ${Date(weather.sunrise * 1000)}")
            Text("Stored Sunset: ${Date(weather.sunset * 1000)}")
        }
    }
}
