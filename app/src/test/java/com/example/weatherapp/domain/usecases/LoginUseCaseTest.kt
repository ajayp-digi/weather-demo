package com.example.weatherapp.domain.usecases
import com.example.weatherapp.data.db.User
import com.example.weatherapp.data.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class LoginUseCaseTest {

    private lateinit var loginUseCase: LoginUseCase
    private val userRepository = mock(UserRepository::class.java)

    @Before
    fun setUp() {
        loginUseCase = LoginUseCase(userRepository)
    }

    @Test
    fun testLogin_success() = runBlocking {
        // Arrange
        val mockUser = User(email = "testUser@gmail.com", password = "testPass")
        `when`(userRepository.login("testUser@gmail.com", "testPass")).thenReturn(mockUser)

        // Act
        val result = loginUseCase.login("testUser@gmail.com", "testPass")

        // Assert
        assertEquals(mockUser, result)
    }

    @Test
    fun testLogin_failure() = runBlocking {
        // Arrange
        `when`(userRepository.login("testUser@gmail.com", "invalidPass")).thenReturn(null)

        // Act
        val result = loginUseCase.login("testUser@gmail.com", "invalidPass")

        // Assert
        assertEquals(null, result)
    }
}
