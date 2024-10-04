package com.example.weatherapp.ui.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.db.WeatherData
import com.example.weatherapp.data.domain.usercase.FetchWeatherUseCase
import com.example.weatherapp.data.domain.usercase.GetStoredWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.utils.DEFAULT_WEATHER_DESTINATION
import com.example.weatherapp.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val fetchWeatherUseCase: FetchWeatherUseCase,
    private val getStoredWeatherUseCase: GetStoredWeatherUseCase
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherData?>(null)
    val weatherState: StateFlow<WeatherData?> = _weatherState

    private val _storedWeatherState = MutableStateFlow<WeatherData?>(null)
    val storedWeatherState: StateFlow<WeatherData?> = _storedWeatherState

    fun fetchWeather(lat: Double, lon: Double, apiKey: String) {
        viewModelScope.launch {
            _weatherState.value = fetchWeatherUseCase.fetchWeather(lat, lon, apiKey)
        }
    }

    fun getStoredWeather() {
        viewModelScope.launch {
            _storedWeatherState.value = getStoredWeatherUseCase.getStoredWeather()
        }
    }
}
