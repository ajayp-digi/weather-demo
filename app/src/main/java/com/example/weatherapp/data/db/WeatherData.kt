package com.example.weatherapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_data")
data class WeatherData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val city: String,
    val country: String,
    val temperature: Double,
    val sunrise: Long,
    val sunset: Long
)