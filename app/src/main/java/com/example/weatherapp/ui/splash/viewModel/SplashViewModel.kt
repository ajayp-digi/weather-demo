package com.example.weatherapp.ui.splash.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.usercase.CheckIfUserLoggedInUseCase
import com.example.weatherapp.domain.usercase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val useCase: CheckIfUserLoggedInUseCase): ViewModel() {

    fun scheduleTimer(callback: (isLoggedIn: Boolean) -> Unit) {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                viewModelScope.launch {
                    val isLoggedIn = checkIfAlreadyLoggedIn()
                    callback(isLoggedIn)

                }
            }
        }, 2000)
    }

    suspend fun checkIfAlreadyLoggedIn(): Boolean {
        return useCase.checkIfUserLoggedIn()
    }
}