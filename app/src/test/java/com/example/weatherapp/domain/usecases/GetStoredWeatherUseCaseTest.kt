package com.example.weatherapp.domain.usecases
import com.example.weatherapp.data.db.WeatherDao
import com.example.weatherapp.data.db.WeatherData
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class GetStoredWeatherUseCaseTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var weatherRepository: WeatherRepository

    @Mock
    lateinit var weatherDao: WeatherDao

    private lateinit var getStoredWeatherUseCase: GetStoredWeatherUseCase

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var testScope: TestScope

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        testScope = TestScope(testDispatcher)
        getStoredWeatherUseCase = GetStoredWeatherUseCase(weatherRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGetStoredWeather_success() = testScope.runTest {
        // Arrange
        val mockStoredWeatherList = listOf(WeatherData(
            city = "Mohali",
            country = "India",
            temperature = 25.0,
            sunrise = System.currentTimeMillis(),
            sunset = System.currentTimeMillis(),
            description = "",
            icon = ""
        ))
        `when`(weatherRepository.getStoredWeatherData()).thenReturn(flowOf(Result.Success(mockStoredWeatherList)))

        // Act
        val result = getStoredWeatherUseCase.getStoredWeather()
        advanceUntilIdle()

        // Assert
        result.collect {
            assertEquals(Result.Success(mockStoredWeatherList), it)
        }
    }
}
