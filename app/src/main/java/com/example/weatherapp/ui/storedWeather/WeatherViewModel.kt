package com.example.weatherapp.ui.storedWeather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.db.WeatherData
import com.example.weatherapp.data.domain.usercase.FetchWeatherUseCase
import com.example.weatherapp.data.domain.usercase.GetStoredWeatherUseCase
import com.example.weatherapp.utils.Result
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val fetchWeatherUseCase: FetchWeatherUseCase,
    private val getStoredWeatherUseCase: GetStoredWeatherUseCase
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherData?>(null)
    val weatherState: StateFlow<WeatherData?> = _weatherState

    private val _storedWeatherState = MutableStateFlow<WeatherData?>(null)
    val storedWeatherState: StateFlow<WeatherData?> = _storedWeatherState

    fun fetchWeather(lat: Double, lon: Double, apiKey: String) {
        viewModelScope.launch {
            fetchWeatherUseCase.fetchWeather(lat, lon, apiKey).collect{ result ->
                when (result) {
                    is Result.Success -> {
                        _weatherState.value = result.data
                        Log.d(" aPiResponse data: : ", Gson().toJson(result.data.toString()))

                    }

                    is Result.Error -> {
                        Log.d(" error  : ", result.errorMessage)
                        //  _storedWeatherState.value = result.errorMessage
                        //  _uiState.value = WeatherUiState(errorMessage = result.errorMessage)
                    }

                    Result.Loading -> {
                        Log.d(" loading   : ","" )
                        //  _uiState.value = WeatherUiState(isLoading = true)
                    }

                    null -> TODO()
                }

            }
        }
    }

      fun getStoredWeather() {
          viewModelScope.launch {
              getStoredWeatherUseCase.getStoredWeather().collect { result ->
                  when (result) {
                      is Result.Success -> {
                          _storedWeatherState.value = result.data
                          Log.d(" aPiResponse data: : ", Gson().toJson(result.data.toString()))
                      }

                      is Result.Error -> {
                          Log.d(" error  : ", result.errorMessage)

                          //    _storedWeatherState.value = result.errorMessage
                          //  _uiState.value = WeatherUiState(errorMessage = result.errorMessage)
                      }

                      Result.Loading -> {
                          Log.d(" loading   : ","" )
                          //  _uiState.value = WeatherUiState(isLoading = true)
                      }

                      null -> TODO()
                  }

              }
          }

    }
}
