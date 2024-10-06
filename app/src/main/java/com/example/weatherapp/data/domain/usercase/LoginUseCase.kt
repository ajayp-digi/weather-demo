package com.example.weatherapp.data.domain.usercase

import com.example.weatherapp.data.db.User
import com.example.weatherapp.data.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun login(username: String, password: String): User? {
        return userRepository.login(username, password)
    }
}