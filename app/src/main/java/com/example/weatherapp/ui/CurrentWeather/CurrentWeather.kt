package com.example.weatherapp.ui.CurrentWeather

import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.ui.main.WeatherViewModel
import java.util.Date

@Composable
fun CurrentWeather(viewModel: WeatherViewModel) {
    val context = LocalContext.current
    val location = remember { mutableStateOf(Location("dummy")) }
    val weatherState by viewModel.weatherState.collectAsState()

    // Get current location (using Location API or fused location provider)
    // Example: Fetch weather data from API
    LaunchedEffect(Unit) {
        // Assume lat and lon are retrieved via location services
        viewModel.fetchWeather(lat = location.value.latitude, lon = location.value.longitude, apiKey = BuildConfig.API_KEY)
    }

    weatherState?.let { weather ->
        Column {
            Text("City: ${weather.city}")
            Text("Country: ${weather.country}")
            Text("Temperature: ${weather.temperature}°C")
            Text("Sunrise: ${Date(weather.sunrise * 1000)}")
            Text("Sunset: ${Date(weather.sunset * 1000)}")
        }
    }
}
