package com.example.weatherapp.domain.usecases

import com.example.weatherapp.data.db.User
import com.example.weatherapp.data.repository.UserRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun registerUser(email: String, password: String): Boolean {
        val user = userRepository.getUserByEmail(email)
        return if (user == null) {
            userRepository.insertUser(User(email = email, password = password))
            true
        } else {
            false // User already exists
        }
    }
}
