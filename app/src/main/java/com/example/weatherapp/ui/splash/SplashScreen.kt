package com.example.weatherapp.ui.splash

import android.content.Context
import android.window.SplashScreen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.R
import com.example.weatherapp.ui.login.viewModel.LoginViewModel
import com.example.weatherapp.ui.splash.viewModel.SplashViewModel
import com.example.weatherapp.ui.theme.Dimensions
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onProceed: (value: Boolean) -> Unit,
) {

    LaunchedEffect(key1 = viewModel) {
        viewModel.scheduleTimer { isLoggedIn ->
            onProceed(isLoggedIn)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.SIXTEEN_DP),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.app_name), color = Color.Black, fontWeight = FontWeight.Bold, fontSize = Dimensions.TWENTY_TWO_SP)
    }

}