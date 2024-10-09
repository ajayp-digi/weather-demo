package com.example.weatherapp

import com.example.weatherapp.data.db.User
import com.example.weatherapp.data.db.UserDao
import com.example.weatherapp.data.managers.LocationManager
import com.example.weatherapp.data.repository.UserRepository
import com.example.weatherapp.ui.weather.viewModel.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@ExperimentalCoroutinesApi
class UserRepositoryTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var userDao: UserDao

    private lateinit var userRepository: UserRepository

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var testScope: TestScope

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        testScope = TestScope(testDispatcher)
        userRepository = UserRepository(userDao)
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher to the default dispatcher
        Dispatchers.resetMain()
    }

    @Test
    fun `insertUser should call UserDao insertUser`() = testScope.runTest {
        // Arrange
        val user = User(id = 1, email = "test@example.com", password = "password123")

        // Act
        userRepository.insertUser(user)

        // Assert
        Mockito.verify(userDao).insertUser(user)
    }

    @Test
    fun `getUserByEmail should return user when UserDao returns user`() = testScope.runTest {
        // Arrange
        val email = "test@example.com"
        val user = User(id = 1, email = email, password = "password123")
        Mockito.`when`(userDao.getUserByEmail(email)).thenReturn(user)

        // Act
        val result = userRepository.getUserByEmail(email)

        // Assert
        assertEquals(user, result)
        Mockito.verify(userDao).getUserByEmail(email)
    }

    @Test
    fun `getUserByEmail should return null when UserDao returns null`() = testScope.runTest {
        // Arrange
        val email = "nonexistent@example.com"
        Mockito.`when`(userDao.getUserByEmail(email)).thenReturn(null)

        // Act
        val result = userRepository.getUserByEmail(email)

        // Assert
        assertNull(result)
        Mockito.verify(userDao).getUserByEmail(email)
    }

    @Test
    fun `checkIfUserLoggedIn should return user when UserDao returns user`() = testScope.runTest {
        // Arrange
        val user = User(id = 1, email = "test@example.com", password = "password123")
        Mockito.`when`(userDao.checkIfUserLoggedIn()).thenReturn(user)

        // Act
        val result = userRepository.checkIfUserLoggedIn()

        // Assert
        assertEquals(user, result)
        Mockito.verify(userDao).checkIfUserLoggedIn()
    }

    @Test
    fun `checkIfUserLoggedIn should return null when UserDao returns null`() = testScope.runTest {
        // Arrange
        Mockito.`when`(userDao.checkIfUserLoggedIn()).thenReturn(null)

        // Act
        val result = userRepository.checkIfUserLoggedIn()

        // Assert
        assertNull(result)
        Mockito.verify(userDao).checkIfUserLoggedIn()
    }

    @Test
    fun `login should return user when credentials are correct`() = testScope.runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        val user = User(id = 1, email = email, password = password)
        Mockito.`when`(userDao.login(email, password)).thenReturn(user)

        // Act
        val result = userRepository.login(email, password)

        // Assert
        assertEquals(user, result)
        Mockito.verify(userDao).login(email, password)
    }

    @Test
    fun `login should return null when credentials are incorrect`() = testScope.runTest {
        // Arrange
        val email = "test@example.com"
        val password = "wrongpassword"
        Mockito.`when`(userDao.login(email, password)).thenReturn(null)

        // Act
        val result = userRepository.login(email, password)

        // Assert
        assertNull(result)
        Mockito.verify(userDao).login(email, password)
    }
}
