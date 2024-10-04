package com.example.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.ui.login.LoginScreen
import com.example.weatherapp.ui.register.RegisterScreen
import com.example.weatherapp.ui.main.HomeScreen

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "register") {
        composable("register") {
            RegisterScreen(
                onRegistrationSuccess = { navController.navigate("login") },
                onNavigateToLogin = { navController.navigate("login") }
            )
        }
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("home") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("home") {
           // WeatherTabsScreen()
            HomeScreen()
        }
    }
}
