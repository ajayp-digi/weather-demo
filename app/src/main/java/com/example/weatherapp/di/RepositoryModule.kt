package com.example.weatherapp.di

import com.example.weatherapp.data.db.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.weatherapp.data.network.WeatherApiService
import com.example.weatherapp.data.repository.DefaultWeatherRepository
import com.example.weatherapp.data.repository.WeatherRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(weatherApi: WeatherApiService,weatherDao: WeatherDao): WeatherRepository =
        DefaultWeatherRepository(weatherApi,weatherDao)
}