package com.example.weatherapp.data.repository

import com.example.weatherapp.data.db.WeatherData
import com.example.weatherapp.utils.Result
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun fetchWeatherFromApi(lat: Double, lon: Double, apiKey: String): Flow<Result<WeatherData>?>
    suspend  fun getStoredWeatherData(): Flow<Result<WeatherData>?>
}