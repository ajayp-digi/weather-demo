package com.example.weatherapp.domain.usercase

import com.example.weatherapp.data.db.WeatherData
import com.example.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStoredWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend fun getStoredWeather(): Flow<com.example.weatherapp.utils.Result<List<WeatherData?>>> {
        return weatherRepository.getStoredWeatherData()
    }


}