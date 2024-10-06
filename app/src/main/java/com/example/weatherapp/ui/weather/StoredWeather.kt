package com.example.weatherapp.ui.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.R
import com.example.weatherapp.ui.weather.state.StoreWeatherState
import com.example.weatherapp.ui.weather.viewModel.WeatherViewModel
import com.example.weatherapp.utils.iconUrl
import java.util.Date

@Composable
fun StoredWeather(viewModel: WeatherViewModel = hiltViewModel()) {
    val storedWeatherState by viewModel.storedWeatherState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getStoredWeather()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (storedWeatherState) {
            is StoreWeatherState.Loading -> {
                CircularProgressIndicator()
            }

            is StoreWeatherState.Error -> {
                val message = (storedWeatherState as StoreWeatherState.Error).message
                Text(text = "Error: $message")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    viewModel.getStoredWeather()
                }) {
                    Text(stringResource(R.string.retry))
                }
            }

            is StoreWeatherState.Success -> {
                val storedWeatherData =
                    (storedWeatherState as StoreWeatherState.Success).weatherData

                storedWeatherData.let {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(it) { weather ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (weather != null) {
                                        Image(
                                            painter = rememberAsyncImagePainter(iconUrl(weather.icon)),
                                            contentDescription = "Weather Icon",
                                            modifier = Modifier
                                                .size(64.dp)
                                                .padding(top = 8.dp),
                                            contentScale = ContentScale.Fit
                                        )
                                    }
                                    if (weather != null) {
                                        Text(text = weather.description)
                                    }
                                }


                            }
                            Spacer(modifier = Modifier.height(8.dp)) // Space between items

                            Column {
                                Text("City: ${weather?.city}")
                                Text("Country: ${weather?.country}")
                                Text("Temperature: ${weather?.temperature}°C")
                                if (weather != null) {
                                    Text("Sunrise: ${Date(weather.sunrise * 1000)}")
                                }
                                if (weather != null) {
                                    Text("Sunset: ${Date(weather.sunset * 1000)}")
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp)) // Space between items
                        }
                    }
                }

            }
        }
    }

}
