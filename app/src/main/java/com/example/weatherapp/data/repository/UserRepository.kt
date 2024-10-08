package com.example.weatherapp.data.repository

import com.example.weatherapp.data.db.User
import com.example.weatherapp.data.db.UserDao
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun checkIfUserLoggedIn(): User? {
        return userDao.checkIfUserLoggedIn()
    }

    suspend fun login(email: String, password: String): User? {
        return userDao.login(email, password)
    }
}