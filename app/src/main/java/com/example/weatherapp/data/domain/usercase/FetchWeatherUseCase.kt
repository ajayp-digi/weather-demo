package com.example.weatherapp.data.domain.usercase

import com.example.weatherapp.data.db.WeatherData
import com.example.weatherapp.data.repository.WeatherRepository
import javax.inject.Inject

class FetchWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend fun fetchWeather(lat: Double, lon: Double, apiKey: String): WeatherData {
        return weatherRepository.fetchWeatherFromApi(lat, lon, apiKey)
    }
}
