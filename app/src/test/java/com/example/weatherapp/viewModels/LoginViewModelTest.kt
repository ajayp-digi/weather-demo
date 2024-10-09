package com.example.weatherapp.viewModels

import com.example.weatherapp.data.db.User
import com.example.weatherapp.domain.usecases.LoginUseCase
import com.example.weatherapp.ui.login.viewModel.LoginState
import com.example.weatherapp.ui.login.viewModel.LoginViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class LoginViewModelTest {

    private lateinit var loginUseCase: LoginUseCase

    private lateinit var loginViewModel: LoginViewModel


    @Before
    fun setUp() {
        loginUseCase = mock(LoginUseCase::class.java)
        loginViewModel = LoginViewModel(loginUseCase)
    }

    @Test
    fun testLogin_success() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Arrange
        `when`(loginUseCase.login("test@gmail.com", "testPass")).thenReturn(User(0, "test@gmail.com",  "testPass"))

        // Act
        loginViewModel.login("test@gmail.com", "testPass")

        // Assert
        assertEquals(true, loginViewModel.loginState.value is LoginState.Success)
    }

    @Test
    fun testLogin_failure() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Arrange
        `when`(loginUseCase.login("test@gmail.com", "testPass")).thenReturn(null)

        // Act
        loginViewModel.login("test@gmail.com", "testPass")

        // Assert
        assertEquals(true, loginViewModel.loginState.value is LoginState.Failure)
    }

    @Test
    fun testLogin_stateFlow() = runTest {

        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Arrange
        `when`(loginUseCase.login("test@gmail.com", "testPass")).thenReturn(User(0, "test@gmail.com", "testPass"))

        // Act
        loginViewModel.login("test@gmail.com", "testPass")

        // Assert
        assertEquals(true, loginViewModel.loginState.value is LoginState.Success)
    }
}
