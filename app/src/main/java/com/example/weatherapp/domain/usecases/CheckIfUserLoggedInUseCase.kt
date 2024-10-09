package com.example.weatherapp.domain.usecases

import com.example.weatherapp.data.repository.UserRepository
import javax.inject.Inject

class CheckIfUserLoggedInUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun checkIfUserLoggedIn(): Boolean {
        return userRepository.checkIfUserLoggedIn() != null
    }
}