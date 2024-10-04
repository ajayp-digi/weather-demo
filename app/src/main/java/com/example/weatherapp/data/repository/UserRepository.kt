package com.example.weatherapp.data.repository

import com.example.weatherapp.data.db.User
import com.example.weatherapp.data.db.UserDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

class UserRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    suspend fun login(username: String, password: String): User? {
        return userDao.login(username, password)
    }
}