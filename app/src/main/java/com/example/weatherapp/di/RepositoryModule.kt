package com.example.weatherapp.di

import com.example.weatherapp.data.db.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.weatherapp.data.network.WeatherApiService
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.example.weatherapp.domain.repository.WeatherRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        weatherApi: WeatherApiService,
        weatherDao: WeatherDao
    ): WeatherRepository =
        WeatherRepositoryImpl(weatherApi, weatherDao)
}