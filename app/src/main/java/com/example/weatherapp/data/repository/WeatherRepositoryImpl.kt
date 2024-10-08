package com.example.weatherapp.data.repository

import com.example.weatherapp.data.db.WeatherDao
import com.example.weatherapp.data.db.WeatherData
import com.example.weatherapp.data.network.WeatherApiService
import com.example.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.example.weatherapp.utils.Result
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApiService,
    private val weatherDao: WeatherDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : WeatherRepository {

    override suspend fun fetchWeatherFromApi(
        lat: Double,
        lon: Double,
        apiKey: String
    ): Flow<Result<WeatherData>> = flow {
        emit(Result.Loading)
        try {
            val response = weatherApi.getWeather(lat, lon, apiKey)

            val weatherData = WeatherData(
                city = response.name ?: "",
                country = response.sys?.country ?: "",
                temperature = response.main?.temp ?: 0.0,
                sunrise = response.sys?.sunrise?.toLong() ?: 0L,
                sunset = response.sys?.sunset?.toLong() ?: 0L,
                description = response.weather.first().description ?: "",
                icon = response.weather.first().icon ?: ""
            )
            weatherDao.insertWeatherData(weatherData)
            emit(Result.Success(weatherData))
        } catch (exception: HttpException) {
            emit(Result.Error(exception.message.orEmpty()))
        } catch (exception: IOException) {
            emit(Result.Error("Please check your network connection and try again!"))
        }
    }.flowOn(dispatcher)


    override suspend fun getStoredWeatherData(): Flow<Result<List<WeatherData?>>> = flow {
        emit(Result.Loading)
        try {
            val result = weatherDao.getLatestWeatherData()
            emit(com.example.weatherapp.utils.Result.Success(result))
        } catch (exception: IOException) {
            emit(com.example.weatherapp.utils.Result.Error("Please check your network connection and try again!"))
        }
    }.flowOn(dispatcher)
}