package com.example.weatherapp.ui.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.Dimensions
import com.example.weatherapp.ui.weather.state.StoreWeatherState
import com.example.weatherapp.ui.weather.viewModel.WeatherViewModel
import com.example.weatherapp.utils.iconUrl
import java.text.SimpleDateFormat
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
            .padding(Dimensions.EIGHT_DP),
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
                Spacer(modifier = Modifier.height(Dimensions.SIXTEEN_DP))
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
                            .fillMaxSize(),
                        contentPadding = PaddingValues(Dimensions.EIGHT_DP),
                        verticalArrangement = Arrangement.spacedBy(Dimensions.EIGHT_DP)
                    ) {
                        items(it) { weather ->
                            weather?.let {
                                Card (elevation = CardDefaults.elevatedCardElevation(defaultElevation = Dimensions.FIVE_DP)){
                                    Column(
                                        verticalArrangement = Arrangement.Top,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(Dimensions.FIVE_DP),
                                    ) {

                                        val formattedDate = SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                                        val formattedSunTime = SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())

                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(Icons.Filled.LocationOn, "")
                                            Spacer(modifier = Modifier.width(Dimensions.SIXTEEN_DP))
                                            Text("${weather.city}, ${weather.country}")
                                        }

                                        Spacer(modifier = Modifier.height(Dimensions.TEN_DP))

                                        Text(formattedDate.format(Date(weather.sunrise * 1000)), fontWeight = FontWeight.Bold, modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.CenterHorizontally), textAlign = TextAlign.Center)

                                        Spacer(modifier = Modifier.height(Dimensions.TEN_DP))

                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                                            Image(painter = painterResource(id = R.drawable.sunrise), contentDescription = "Sunrise Icon", modifier = Modifier.size(Dimensions.THIRTY_TWO_DP))
                                            Spacer(modifier = Modifier.width(Dimensions.FIVE_DP))
                                            Text(formattedSunTime.format(Date(weather.sunrise * 1000)), fontWeight = FontWeight.Bold)

                                            Spacer(modifier = Modifier.width(Dimensions.SIXTEEN_DP))

                                            Image(painter = painterResource(id = R.drawable.sunset), contentDescription = "Sunrise Icon", modifier = Modifier.size(Dimensions.THIRTY_TWO_DP))
                                            Spacer(modifier = Modifier.width(Dimensions.FIVE_DP))
                                            Text(formattedSunTime.format(Date(weather.sunset * 1000)), fontWeight = FontWeight.Bold)
                                        }

                                        Spacer(modifier = Modifier.height(Dimensions.TEN_DP))

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Image(
                                                painter = rememberAsyncImagePainter(iconUrl(weather.icon)),
                                                contentDescription = "Weather Icon",
                                                modifier = Modifier
                                                    .size(Dimensions.ONE_FIFTY_DP),
                                                contentScale = ContentScale.Fit
                                            )

                                        }

                                        Spacer(modifier = Modifier.height(Dimensions.TEN_DP))

                                        Column(horizontalAlignment = Alignment.Start) {
                                            Text(text = weather.description.capitalize(Locale.current))
                                            Text(
                                                "${weather.temperature}°C",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = Dimensions.FORTY_SP
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

}
