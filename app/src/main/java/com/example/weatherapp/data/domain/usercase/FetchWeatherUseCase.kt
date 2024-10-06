package com.example.weatherapp.data.domain.usercase

import com.example.weatherapp.data.db.WeatherDao
import com.example.weatherapp.data.db.WeatherData
import com.example.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val weatherDao: WeatherDao
) {
    suspend fun fetchWeather(
        lat: Double,
        lon: Double,
        apiKey: String
    ): Flow<com.example.weatherapp.utils.Result<WeatherData>?> {
        return weatherRepository.fetchWeatherFromApi(lat, lon, apiKey)
    }

}
