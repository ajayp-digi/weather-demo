package com.example.weatherapp.data.domain.usercase

import com.example.weatherapp.data.db.WeatherData
import com.example.weatherapp.data.repository.WeatherRepository
import javax.inject.Inject

class GetStoredWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend fun getStoredWeather(): WeatherData? {
        return weatherRepository.getStoredWeatherData()
    }
}