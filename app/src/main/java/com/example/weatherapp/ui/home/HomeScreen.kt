package com.example.weatherapp.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.R
import com.example.weatherapp.ui.weather.CurrentWeather
import com.example.weatherapp.ui.weather.StoredWeather
import com.example.weatherapp.ui.weather.viewModel.WeatherViewModel

@Composable
fun HomeScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.current_weather),
        stringResource(R.string.stored_weather)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 15.dp)
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
            0 -> CurrentWeather(viewModel)
            1 -> StoredWeather(viewModel)
        }
    }
}


