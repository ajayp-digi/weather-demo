package com.example.weatherapp.domain.usecases
import com.example.weatherapp.data.db.User
import com.example.weatherapp.data.db.WeatherDao
import com.example.weatherapp.data.repository.UserRepository
import com.example.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class CheckIfUserLoggedInUseCaseTest {

    @Mock
    lateinit var userRepository: UserRepository

    private lateinit var checkIfUserLoggedInUseCase: CheckIfUserLoggedInUseCase

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
        checkIfUserLoggedInUseCase = CheckIfUserLoggedInUseCase(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testCheckIfUserLoggedIn_userExists() = testScope.runTest {
        // Arrange
        val mockUser = User(email = "loggedInUser@example.com", password = "testPass")
        `when`(userRepository.checkIfUserLoggedIn()).thenReturn(mockUser)

        // Act
        val result = checkIfUserLoggedInUseCase.checkIfUserLoggedIn()
        advanceUntilIdle()
        // Assert
        assertEquals(true, result)
    }

    @Test
    fun testCheckIfUserLoggedIn_noUser() = testScope.runTest {
        // Arrange
        `when`(userRepository.checkIfUserLoggedIn()).thenReturn(null)

        // Act
        val result = checkIfUserLoggedInUseCase.checkIfUserLoggedIn()
        advanceUntilIdle()
        // Assert
        assertEquals(false, result)
    }
}
