package com.example.weatherapp.data.repository
import com.example.weatherapp.data.db.WeatherDao
import com.example.weatherapp.data.db.WeatherData
import com.example.weatherapp.data.network.WeatherApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.example.weatherapp.utils.Result
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DefaultWeatherRepository @Inject constructor(
    private val weatherApi: WeatherApiService,
    private val weatherDao: WeatherDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : WeatherRepository {

    override suspend fun fetchWeatherFromApi(
        lat: Double,
        lon: Double,
        apiKey: String
    ):Flow<Result<WeatherData>> = flow {
        emit(com.example.weatherapp.utils.Result.Loading)
        try {
            val response = weatherApi.getWeather(lat, lon, apiKey)

            val weatherData = WeatherData(
                city = response.name?:"",
                country = response.sys?.country?:"",
                temperature = response.main?.temp?:0.0,
                sunrise = response.sys?.sunrise?.toLong()?:0L,
                sunset = response.sys?.sunset?.toLong()?:0L
            )
            weatherDao.insertWeatherData(weatherData)
            emit(Result.Success(weatherData))
        } catch (exception: HttpException) {
            emit(Result.Error(exception.message.orEmpty()))
        } catch (exception: IOException) {
            emit(Result.Error("Please check your network connection and try again!"))
        }
    }.flowOn(dispatcher)


    override suspend fun getStoredWeatherData(): Flow<Result<WeatherData>?> = flow {
        emit(com.example.weatherapp.utils.Result.Loading)
        try {
        val result = weatherDao.getLatestWeatherData()?:WeatherData(city = "",
            country = "",
            temperature = 0.0,
            sunrise = 0L,
            sunset = 0L);
            emit(com.example.weatherapp.utils.Result.Success(result))
        } catch (exception: IOException) {
            emit(com.example.weatherapp.utils.Result.Error("Please check your network connection and try again!"))
        }
    }.flowOn(dispatcher)
}