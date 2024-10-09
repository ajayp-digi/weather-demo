package com.example.weatherapp.ui.weather.viewModel

import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.domain.usecases.FetchWeatherUseCase
import com.example.weatherapp.domain.usecases.GetStoredWeatherUseCase
import com.example.weatherapp.data.managers.LocationManager
import com.example.weatherapp.ui.weather.state.StoreWeatherState
import com.example.weatherapp.ui.weather.state.WeatherState
import com.example.weatherapp.utils.NetworkUtil
import com.example.weatherapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val fetchWeatherUseCase: FetchWeatherUseCase,
    private val getStoredWeatherUseCase: GetStoredWeatherUseCase,
    private val locationManager: LocationManager
) : ViewModel() {

    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location

    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState

    private val _storedWeatherState = MutableStateFlow<StoreWeatherState>(StoreWeatherState.Loading)
    val storedWeatherState: StateFlow<StoreWeatherState> = _storedWeatherState


    fun fetchWeather(lat: Double, lon: Double, apiKey: String, context: Context) {
        viewModelScope.launch {
            if (NetworkUtil.isNetworkAvailable(context)) {

                fetchWeatherUseCase.fetchWeather(lat, lon, apiKey).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _weatherState.value = WeatherState.Success(result.data)

                        }

                        is Result.Error -> {
                            _weatherState.value = WeatherState.Error(result.errorMessage)
                        }

                        Result.Loading -> {
                            _weatherState.value = WeatherState.Loading
                        }

                        null -> _weatherState.value =
                            WeatherState.Error(context.getString(R.string.data_not_found))
                    }

                }
            } else {
                _weatherState.value = WeatherState.Error(context.getString(R.string.internet_msg))
            }
        }
    }

    fun getStoredWeather() {
        viewModelScope.launch {
            getStoredWeatherUseCase.getStoredWeather().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _storedWeatherState.value = StoreWeatherState.Success(result.data)
                    }

                    is Result.Error -> {
                        _storedWeatherState.value = StoreWeatherState.Error(result.errorMessage)
                    }

                    Result.Loading -> {}

                }

            }
        }

    }

    fun fetchLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loc = locationManager.getLastKnownLocation().await()
                _location.value = loc
            } catch (e: Exception) {
                e.printStackTrace() // Handle any errors here
            }
        }
    }
}
