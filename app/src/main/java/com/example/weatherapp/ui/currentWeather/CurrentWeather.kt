package com.example.weatherapp.ui.currentWeather

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.ui.storedWeather.WeatherViewModel
import java.util.Date

@Composable
fun CurrentWeather(viewModel: WeatherViewModel = hiltViewModel()) {
    val context = LocalContext.current
   // val location = remember { mutableStateOf(Location("Barara")) }
    val weatherState by viewModel.weatherState.collectAsState()

    LaunchedEffect(Unit) {
       // viewModel.fetchWeather(lat = location.value.latitude, lon = location.value.longitude, apiKey = BuildConfig.API_KEY)
        viewModel.fetchWeather(lat = 30.2117786, lon = 77.0420481, apiKey = BuildConfig.API_KEY)
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
