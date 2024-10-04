package com.example.weatherapp.data.repository

import com.example.weatherapp.data.db.WeatherDao
import com.example.weatherapp.data.db.WeatherData
import com.example.weatherapp.data.network.WeatherApiService
import com.example.weatherapp.model.Weather
import com.example.weatherapp.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherDao: WeatherDao,
    private val weatherApiService: WeatherApiService
) {

    suspend fun fetchWeatherFromApi(lat: Double, lon: Double, apiKey: String): WeatherData {
        val response = weatherApiService.getWeather(lat, lon, apiKey)
        val weatherData = WeatherData(
            city = response.name,
            country = response.sys.country,
            temperature = response.main.temp,
            sunrise = response.sys.sunrise,
            sunset = response.sys.sunset
        )
        weatherDao.insertWeatherData(weatherData)
        return weatherData
    }

    suspend fun getStoredWeatherData(): WeatherData? {
        return weatherDao.getLatestWeatherData()
    }
}