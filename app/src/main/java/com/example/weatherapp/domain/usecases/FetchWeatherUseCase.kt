package com.example.weatherapp.domain.usecases

import com.example.weatherapp.data.db.WeatherData
import com.example.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {
    suspend fun fetchWeather(
        lat: Double,
        lon: Double,
        apiKey: String
    ): Flow<com.example.weatherapp.utils.Result<WeatherData>?> {
        return weatherRepository.fetchWeatherFromApi(lat, lon, apiKey)
    }

}
