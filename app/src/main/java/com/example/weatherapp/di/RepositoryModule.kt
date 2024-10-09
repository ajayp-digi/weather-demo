package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.data.db.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.weatherapp.data.network.WeatherApiService
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.example.weatherapp.domain.repository.WeatherRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        @ApplicationContext context: Context,
        weatherApi: WeatherApiService,
        weatherDao: WeatherDao
    ): WeatherRepository =
        WeatherRepositoryImpl(context, weatherApi, weatherDao)
}