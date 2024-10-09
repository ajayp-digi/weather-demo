package com.example.weatherapp.viewModels
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.domain.usecases.RegisterUseCase
import com.example.weatherapp.ui.register.viewModel.RegisterViewModel
import com.example.weatherapp.ui.register.viewModel.RegistrationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class RegisterViewModelTest {


    private lateinit var registerUseCase: RegisterUseCase

    private lateinit var registerViewModel: RegisterViewModel
    private val observer = mock(Observer::class.java)

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
        val mockResult = com.example.weatherapp.utils.Result.Success(true)
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
        val exception = Exception("User already exists")
        `when`(registerUseCase.registerUser("test@example.com", "testPass")).thenReturn(false)

        // Act
        registerViewModel.registerUser("test@example.com", "testPass")

        // Assert
        assert(registerViewModel.registrationState.value is RegistrationState.Failure)
    }
}
