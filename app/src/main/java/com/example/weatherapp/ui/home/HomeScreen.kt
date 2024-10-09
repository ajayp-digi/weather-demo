package com.example.weatherapp.ui.home

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.Dimensions
import com.example.weatherapp.ui.weather.CurrentWeather
import com.example.weatherapp.ui.weather.StoredWeather
import com.example.weatherapp.ui.weather.viewModel.WeatherViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.current_weather),
        stringResource(R.string.stored_weather)
    )
    val location by viewModel.location.collectAsStateWithLifecycle()
    val weatherState by viewModel.weatherState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(location) {
        location?.let {
            viewModel.fetchWeather(
                lat = it.latitude,
                lon = it.longitude,
                apiKey = BuildConfig.API_KEY,
                context = context
            )
        }
    }

    val locationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(locationPermissionState.status) {
        if (locationPermissionState.status.isGranted) {
            viewModel.fetchLocation() // Fetch location once permission is granted
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dimensions.FIFTEEN_DP)
    ) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title) }
                )
            }
        }
        when (selectedTabIndex) {
            0 -> CurrentWeather(locationPermissionState, weatherState) {
                location?.let {
                    viewModel.fetchWeather(
                        lat = it.latitude,
                        lon = it.longitude,
                        apiKey = BuildConfig.API_KEY,
                        context = context
                    )
                }
            }
            1 -> StoredWeather(viewModel)
        }
    }
}


