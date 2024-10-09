package com.example.weatherapp.viewModels
import com.example.weatherapp.domain.usecases.RegisterUseCase
import com.example.weatherapp.ui.register.viewModel.RegisterViewModel
import com.example.weatherapp.ui.register.viewModel.RegistrationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class RegisterViewModelTest {


    private lateinit var registerUseCase: RegisterUseCase

    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setUp() {
        registerUseCase = mock(RegisterUseCase::class.java)
        registerViewModel = RegisterViewModel(registerUseCase)
    }

    @Test
    fun testRegisterUser_success() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Arrange
        `when`(registerUseCase.registerUser("test@example.com", "testPass")).thenReturn(true)

        // Act
        registerViewModel.registerUser("test@example.com", "testPass")

        // Assert
        assert(registerViewModel.registrationState.value is RegistrationState.Success)
    }

    @Test
    fun testRegisterUser_failure() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Arrange
        `when`(registerUseCase.registerUser("test@example.com", "testPass")).thenReturn(false)

        // Act
        registerViewModel.registerUser("test@example.com", "testPass")

        // Assert
        assert(registerViewModel.registrationState.value is RegistrationState.Failure)
    }
}
