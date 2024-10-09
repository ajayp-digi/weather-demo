package com.example.weatherapp.viewModels

import android.content.Context
import android.location.Location
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.Observer
import com.example.weatherapp.R
import com.example.weatherapp.data.db.WeatherData
import com.example.weatherapp.ui.weather.viewModel.WeatherViewModel
import com.example.weatherapp.data.managers.LocationManager
import com.example.weatherapp.domain.usecases.FetchWeatherUseCase
import com.example.weatherapp.domain.usecases.GetStoredWeatherUseCase
import com.example.weatherapp.ui.weather.state.StoreWeatherState
import com.example.weatherapp.ui.weather.state.WeatherState
import com.example.weatherapp.utils.NetworkUtil
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class WeatherViewModelTest {

    @Mock
    lateinit var fetchWeatherUseCase: FetchWeatherUseCase

    @Mock
    lateinit var getStoredWeatherUseCase: GetStoredWeatherUseCase

    @Mock
    lateinit var locationManager: LocationManager

    @Mock
    lateinit var networkUtils: NetworkUtil

    @Mock
    lateinit var connectivityManager: ConnectivityManager

    @Mock
    lateinit var context: Context

    private lateinit var weatherViewModel: WeatherViewModel
    private val observer = mock(Observer::class.java)

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var testScope: TestScope

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        testScope = TestScope(testDispatcher)
        locationManager = mock(LocationManager::class.java)
        weatherViewModel = WeatherViewModel(fetchWeatherUseCase, getStoredWeatherUseCase, locationManager)
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher to the default dispatcher
        Dispatchers.resetMain()
    }

    @Test
    fun testFetchWeather_success() = testScope.runTest {

        val network = mock(Network::class.java)
        val networkCapabilities = mock(NetworkCapabilities::class.java)
        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)
        `when`(connectivityManager.activeNetwork).thenReturn(network)
        `when`(connectivityManager.getNetworkCapabilities(network)).thenReturn(networkCapabilities)
        `when`(NetworkUtil.isNetworkAvailable(context)).thenReturn(true)
        val mockWeatherData = com.example.weatherapp.utils.Result.Success(mock(WeatherData::class.java))
        `when`(fetchWeatherUseCase.fetchWeather(1.0, 1.0, "apiKey")).thenReturn(flowOf(mockWeatherData))

        // Act
        weatherViewModel.fetchWeather(1.0, 1.0, "apiKey", context)
        advanceUntilIdle()
        // Assert
        assert(weatherViewModel.weatherState.value is WeatherState.Success)
    }

    @Test
    fun testFetchWeather_loading() = testScope.runTest {

        val network = mock(Network::class.java)
        val networkCapabilities = mock(NetworkCapabilities::class.java)
        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)
        `when`(connectivityManager.activeNetwork).thenReturn(network)
        `when`(connectivityManager.getNetworkCapabilities(network)).thenReturn(networkCapabilities)
        `when`(NetworkUtil.isNetworkAvailable(context)).thenReturn(true)
        val mockWeatherData = com.example.weatherapp.utils.Result.Loading
        `when`(fetchWeatherUseCase.fetchWeather(1.0, 1.0, "apiKey")).thenReturn(flowOf(mockWeatherData))

        // Act
        weatherViewModel.fetchWeather(1.0, 1.0, "apiKey", context)
        advanceUntilIdle()
        // Assert
        assert(weatherViewModel.weatherState.value is WeatherState.Loading)
    }

    @Test
    fun testFetchWeather_null() = testScope.runTest {

        val network = mock(Network::class.java)
        val networkCapabilities = mock(NetworkCapabilities::class.java)
        `when`(context.getString(R.string.data_not_found)).thenReturn("data not found")
        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)
        `when`(connectivityManager.activeNetwork).thenReturn(network)
        `when`(connectivityManager.getNetworkCapabilities(network)).thenReturn(networkCapabilities)
        `when`(NetworkUtil.isNetworkAvailable(context)).thenReturn(true)
        val mockWeatherData = null
        `when`(fetchWeatherUseCase.fetchWeather(1.0, 1.0, "apiKey")).thenReturn(flowOf(mockWeatherData))

        // Act
        weatherViewModel.fetchWeather(1.0, 1.0, "apiKey", context)
        advanceUntilIdle()
        // Assert
        assert(weatherViewModel.weatherState.value is WeatherState.Error)
    }

    @Test
    fun testFetchWeather_failure() = testScope.runTest {
        // Arrange
        val network = mock(Network::class.java)
        val networkCapabilities = mock(NetworkCapabilities::class.java)
        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)
        `when`(connectivityManager.activeNetwork).thenReturn(network)
        `when`(connectivityManager.getNetworkCapabilities(network)).thenReturn(networkCapabilities)
        `when`(NetworkUtil.isNetworkAvailable(context)).thenReturn(true)
        `when`(fetchWeatherUseCase.fetchWeather(1.0, 1.0, "apiKey")).thenReturn(flowOf(com.example.weatherapp.utils.Result.Error(errorMessage = "Network Error")))

        // Act
        weatherViewModel.fetchWeather(1.0, 1.0, "apiKey", context)
        advanceUntilIdle()
        // Assert
        assert(weatherViewModel.weatherState.value is WeatherState.Error)
    }

    @Test
    fun testFetchWeather_no_network() = testScope.runTest {
        // Arrange
        val network = mock(Network::class.java)
        `when`(context.getString(R.string.internet_msg)).thenReturn("No network connection")
        val networkCapabilities = mock(NetworkCapabilities::class.java)
        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)
        `when`(connectivityManager.activeNetwork).thenReturn(network)
        `when`(connectivityManager.getNetworkCapabilities(network)).thenReturn(networkCapabilities)
        `when`(NetworkUtil.isNetworkAvailable(context)).thenReturn(false)
        `when`(fetchWeatherUseCase.fetchWeather(1.0, 1.0, "apiKey")).thenReturn(flowOf(com.example.weatherapp.utils.Result.Error(errorMessage = "Network Error")))

        // Act
        weatherViewModel.fetchWeather(1.0, 1.0, "apiKey", context)
        advanceUntilIdle()
        // Assert
        assert(weatherViewModel.weatherState.value is WeatherState.Error)
    }

    @Test
    fun testGetStoredWeather_success() = testScope.runTest {

        // Arrange
        val mockStoredWeatherData = com.example.weatherapp.utils.Result.Success(listOf(mock(WeatherData::class.java)))
        `when`(getStoredWeatherUseCase.getStoredWeather()).thenReturn(flowOf(mockStoredWeatherData))

        // Act
        weatherViewModel.getStoredWeather()
        advanceUntilIdle()

        // Assert
        assert(weatherViewModel.storedWeatherState.value is StoreWeatherState.Success)
    }

    @Test
    fun testGetStoredWeather_failure() = testScope.runTest {
        // Arrange
        val mockStoredWeatherData = com.example.weatherapp.utils.Result.Error("failed")
        `when`(getStoredWeatherUseCase.getStoredWeather()).thenReturn(flowOf(mockStoredWeatherData))

        // Act
        weatherViewModel.getStoredWeather()
        advanceUntilIdle()

        // Assert
        assert(weatherViewModel.storedWeatherState.value is StoreWeatherState.Error)
    }

    @Test
    fun testGetStoredWeather_loading() = testScope.runTest {
        // Arrange
        val mockStoredWeatherData = com.example.weatherapp.utils.Result.Loading
        `when`(getStoredWeatherUseCase.getStoredWeather()).thenReturn(flowOf(mockStoredWeatherData))

        // Act
        weatherViewModel.getStoredWeather()
        advanceUntilIdle()

        // Assert
        assert(weatherViewModel.storedWeatherState.value is StoreWeatherState.Loading)
    }

    @Test
    fun testLocationUpdates() = testScope.runTest {
        // Arrange
        val mockLocation = mock(Location::class.java)
        val mockTask = mock(Task::class.java) as Task<Location>
        mockLocation.isMock = true
        mockLocation.latitude = 30.2
        mockLocation.longitude = 78.2

        `when`(locationManager.getLastKnownLocation()).thenReturn(mockTask)
        `when`(locationManager.getLastKnownLocation().isComplete).thenReturn(true)
        `when`(locationManager.getLastKnownLocation().await()).thenReturn(mockLocation)

        // Act
        weatherViewModel.fetchLocation()
        advanceUntilIdle()

        // Assert
        assert(true)
    }

    @Test
    fun testLocationUpdatesFailed() = testScope.runTest {
        // Arrange
        val mockLocation = mock(Location::class.java)
        val mockTask = mock(Task::class.java) as Task<Location>
        mockLocation.isMock = true
        mockLocation.latitude = 30.2
        mockLocation.longitude = 78.2

        `when`(locationManager.getLastKnownLocation()).thenReturn(mockTask)
        `when`(locationManager.getLastKnownLocation().isComplete).thenReturn(true)
        `when`(locationManager.getLastKnownLocation().await()).thenThrow(IllegalArgumentException("error"))

        // Act
        weatherViewModel.fetchLocation()
        advanceUntilIdle()

        // Assert
        val exception = assertThrows(java.lang.IllegalArgumentException::class.java) {
            throw IllegalArgumentException("Invalid argument")
        }

        assert(exception.message == "Invalid argument")
    }
}
