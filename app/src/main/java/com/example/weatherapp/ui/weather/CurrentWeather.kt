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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.size.Dimension
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.R
import com.example.weatherapp.data.db.WeatherData
import com.example.weatherapp.ui.theme.Dimensions
import com.example.weatherapp.ui.weather.state.WeatherState
import com.example.weatherapp.ui.weather.viewModel.WeatherViewModel
import com.example.weatherapp.utils.iconUrl
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CurrentWeather(viewModel: WeatherViewModel = hiltViewModel(), locationPermissionState: PermissionState,
                   weatherState: WeatherState, fetchLocation: () -> Unit) {
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.SIXTEEN_DP),
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
                        Spacer(modifier = Modifier.height(Dimensions.SIXTEEN_DP))
                        Button(onClick = {
                            fetchLocation()
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
                    Spacer(modifier = Modifier.height(Dimensions.SIXTEEN_DP))
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
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxWidth()
        ) {

            val formattedDate = SimpleDateFormat("dd/MM/yyyy hh:mm a")

            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.LocationOn,"")
                Spacer(modifier = Modifier.width(Dimensions.SIXTEEN_DP))
                Text("${weather.city}, ${weather.country}")
            }

            Spacer(modifier = Modifier.height(Dimensions.TEN_DP))

            Text(formattedDate.format(Date(weather.sunrise * 1000)), fontWeight = FontWeight.Bold, modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally), textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(Dimensions.TEN_DP))

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = rememberAsyncImagePainter(iconUrl(weather.icon)),
                    contentDescription = "Weather Icon",
                    modifier = Modifier
                        .size(Dimensions.TWO_FIFTY_DP),
                    contentScale = ContentScale.Fit
                )

            }

            Spacer(modifier = Modifier.height(Dimensions.TEN_DP))
            
            Column(horizontalAlignment = Alignment.Start) {
                Text(text = weather.description.capitalize(Locale.current))
                Text("${weather.temperature}°C", fontWeight = FontWeight.Bold, fontSize = Dimensions.FORTY_SP)
            }
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
