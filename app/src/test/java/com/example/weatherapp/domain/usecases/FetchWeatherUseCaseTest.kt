package com.example.weatherapp.domain.usecases
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.db.WeatherDao
import com.example.weatherapp.data.db.WeatherData
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onErrorResume
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class FetchWeatherUseCaseTest {

    private lateinit var fetchWeatherUseCase: FetchWeatherUseCase

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
        fetchWeatherUseCase = FetchWeatherUseCase(weatherRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testFetchWeather_success() = testScope.runTest {
        // Arrange
        val mockWeatherData = WeatherData(
            city = "Mohali",
            country = "India",
            temperature = 25.0,
            sunrise = System.currentTimeMillis(),
            sunset = System.currentTimeMillis(),
            description = "",
            icon = ""
        )
        `when`(weatherRepository.fetchWeatherFromApi(1.0, 1.0, "apiKey")).thenReturn(flowOf(Result.Success(mockWeatherData)))

        // Act
        val result = fetchWeatherUseCase.fetchWeather(1.0, 1.0, "apiKey")
        advanceUntilIdle()
        // Assert
        result.collect {
            assertEquals(Result.Success(mockWeatherData), it)
        }
    }

    @Test
    fun testFetchWeather_failure(): Unit = testScope.runTest {
        // Arrange
        val exception = Exception("Network Error")
        `when`(weatherRepository.fetchWeatherFromApi(1.0, 1.0, "apiKey")).thenReturn(flowOf(Result.Error("Network Error")))

        // Act

            fetchWeatherUseCase.fetchWeather(1.0, 1.0, "apiKey")
                .catch {
                    assertEquals("Network Error", it.message)
                }
                .collect {

            }

    }
}
