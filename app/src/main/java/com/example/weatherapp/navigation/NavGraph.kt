package com.example.weatherapp.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.R
import com.example.weatherapp.ui.login.LoginScreen
import com.example.weatherapp.ui.register.RegisterScreen
import com.example.weatherapp.ui.home.HomeScreen
import com.example.weatherapp.ui.splash.SplashScreen

@Composable
fun AppNavigator(context: Context) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = context.getString(R.string.splash_route)) {
        composable(context.getString(R.string.splash_route)) {
            SplashScreen(
                onProceed = { value ->
                    if (value) {
                        navController.navigate(context.getString(R.string.home_route))
                    } else {
                        navController.navigate(context.getString(R.string.register_route))
                    }
                }
            )
        }
        composable(context.getString(R.string.register_route)) {
            RegisterScreen(
                context,
                onRegistrationSuccess = { navController.navigate(context.getString(R.string.login_route)) },
                onNavigateToLogin = { navController.navigate(context.getString(R.string.login_route)) }
            )
        }
        composable(context.getString(R.string.login_route)) {
            LoginScreen(
                context,
                onLoginSuccess = { navController.navigate(context.getString(R.string.home_route)) },
                onNavigateToRegister = { navController.navigate(context.getString(R.string.register_route)) }
            )
        }
        composable(context.getString(R.string.home_route)) {
            HomeScreen()
        }
    }
}
