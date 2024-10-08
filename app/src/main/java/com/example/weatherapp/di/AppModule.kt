package com.example.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.db.AppDatabase
import com.example.weatherapp.data.db.UserDao
import com.example.weatherapp.data.db.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context): AppDatabase {

        val passphrase = SQLiteDatabase.getBytes(BuildConfig.CIPHER_KEY.toCharArray())

        val factory = SupportFactory(passphrase)

        return Room.databaseBuilder(
            app, AppDatabase::class.java, "user_database"
        ).openHelperFactory(factory).build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideWeatherDao(db: AppDatabase): WeatherDao {
        return db.weatherDao()
    }
}