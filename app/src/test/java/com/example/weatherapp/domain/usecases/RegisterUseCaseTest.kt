package com.example.weatherapp.domain.usecases
import com.example.weatherapp.data.db.User
import com.example.weatherapp.data.db.UserDao
import com.example.weatherapp.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class RegisterUseCaseTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var userDao: UserDao

    @Mock
    lateinit var userRepository: UserRepository


    private lateinit var registerUseCase: RegisterUseCase

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var testScope: TestScope

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        testScope = TestScope(testDispatcher)
        registerUseCase = RegisterUseCase(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testRegisterUser_success() = testScope.runTest {
        // Arrange
        val user = User(email = "test@example.com", password = "testPass")
        `when`(userRepository.getUserByEmail("test@example.com")).thenReturn(null)
        `when`(userDao.insertUser(user)).thenReturn(Unit)

        // Act
        val result = registerUseCase.registerUser("test@example.com", "testPass")
        advanceUntilIdle()

        // Assert
        assertEquals(true, result)
    }

    @Test
    fun testRegisterUser_failure_userAlreadyExists() = testScope.runTest {
        // Arrange
        val user = User(email = "test@example.com", password = "testPass")
        `when`(userRepository.getUserByEmail("test@example.com")).thenReturn(user)
        `when`(userRepository.insertUser(user)).thenReturn(Unit)

        // Act
        val result = registerUseCase.registerUser("test@example.com", "testPass")
        advanceUntilIdle()

        // Assert
        assertEquals(false, result)
    }
}
