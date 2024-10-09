package com.example.weatherapp.ui.splash.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.usecases.CheckIfUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val useCase: CheckIfUserLoggedInUseCase): ViewModel() {

    fun scheduleTimer(callback: (isLoggedIn: Boolean) -> Unit) {
                viewModelScope.launch {
                    kotlinx.coroutines.delay(2000)
                    val isLoggedIn = checkIfAlreadyLoggedIn()
                    callback(isLoggedIn)

                }
    }

    suspend fun checkIfAlreadyLoggedIn(): Boolean {
        return useCase.checkIfUserLoggedIn()
    }
}