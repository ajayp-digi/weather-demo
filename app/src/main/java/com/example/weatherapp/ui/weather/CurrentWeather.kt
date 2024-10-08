package com.example.weatherapp.ui.weather

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.R
import com.example.weatherapp.data.db.WeatherData
import com.example.weatherapp.ui.weather.state.WeatherState
import com.example.weatherapp.ui.weather.viewModel.WeatherViewModel
import com.example.weatherapp.utils.iconUrl
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.util.Date

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CurrentWeather(viewModel: WeatherViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val location by viewModel.location.collectAsStateWithLifecycle()
    val weatherState by viewModel.weatherState.collectAsState()
    var isWeatherFetched = rememberSaveable {
        false
    }

    LaunchedEffect(location) {
        if (!isWeatherFetched) {
            location?.let {
                viewModel.fetchWeather(
                    lat = it.latitude,
                    lon = it.longitude,
                    apiKey = BuildConfig.API_KEY,
                    context = context
                )
            }
        }
    }

    LaunchedEffect(key1 = weatherState) {
        if (weatherState is WeatherState.Success) {
            isWeatherFetched = true
        }
    }

    val locationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            viewModel.fetchLocation() // Fetch location once permission is granted
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        when (locationPermissionState.status) {
            is PermissionStatus.Granted -> {
                when (weatherState) {
                    is WeatherState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is WeatherState.Success -> {
                        val weatherData = (weatherState as WeatherState.Success).weatherData
                        weatherInfoPage(weatherData)
                    }

                    is WeatherState.Error -> {
                        val message = (weatherState as WeatherState.Error).message
                        Text(text = "Error: $message")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            location?.let {
                                viewModel.fetchWeather(
                                    lat = it.latitude,
                                    lon = it.longitude,
                                    apiKey = BuildConfig.API_KEY,
                                    context = context
                                )
                            }
                        }) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
            }

            is PermissionStatus.Denied -> {
                if ((locationPermissionState.status as PermissionStatus.Denied).shouldShowRationale) {
                    Text(stringResource(R.string.required_permission_msg))
                    Button(onClick = {
                        locationPermissionState.launchPermissionRequest()
                    }) {
                        Text(stringResource(R.string.request_permission))
                    }
                } else {
                    Text(stringResource(R.string.permission_permanently_denied))
                    Spacer(modifier = Modifier.height(16.dp))
                    OpenAppSettings()
                }
            }
        }
    }
}


@Composable
fun weatherInfoPage(weatherState: WeatherData?) {
    weatherState?.let { weather ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(iconUrl(weather.icon)),
                    contentDescription = "Weather Icon",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(top = 8.dp),
                    contentScale = ContentScale.Fit
                )
                Text(text = weather.description)
            }
        }
        Column {
            Text("City: ${weather.city}")
            Text("Country: ${weather.country}")
            Text("Temperature: ${weather.temperature}°C")
            Text("Sunrise: ${Date(weather.sunrise * 1000)}")
            Text("Sunset: ${Date(weather.sunset * 1000)}")
        }
    }
}

@Composable
fun OpenAppSettings() {
    val context = LocalContext.current

    Button(onClick = {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, intent, null)
    }) {
        Text(stringResource(R.string.openSetting))
    }
}
