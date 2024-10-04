package com.example.weatherapp.data.domain.usercase

import com.example.weatherapp.data.db.User
import com.example.weatherapp.data.repository.UserRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun registerUser(username: String, password: String): Boolean {
        val user = userRepository.getUserByUsername(username)
        return if (user == null) {
            userRepository.insertUser(User(username = username, password = password))
            true
        } else {
            false // User already exists
        }
    }
}
