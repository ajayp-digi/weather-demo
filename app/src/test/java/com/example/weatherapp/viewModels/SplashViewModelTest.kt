package com.example.weatherapp.viewModels

import androidx.lifecycle.Observer

import com.example.weatherapp.domain.usecases.CheckIfUserLoggedInUseCase
import com.example.weatherapp.ui.register.viewModel.RegistrationState
import com.example.weatherapp.ui.splash.viewModel.SplashViewModel
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.time.delay
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.whenever
import java.time.Duration
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class SplashViewModelTest {

    private lateinit var splashViewModel: SplashViewModel

    @Mock
    lateinit var useCase: CheckIfUserLoggedInUseCase

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var testScope: TestScope

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        testScope = TestScope(testDispatcher)
        splashViewModel = SplashViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testCheckIfUserLoggedIn_userExists() = testScope.runTest {
        `when`(useCase.checkIfUserLoggedIn()).thenReturn(true)

        val deferred = CompletableDeferred<Boolean>()

        val callback: (Boolean) -> Unit = { result ->

            deferred.complete(result)

        }

        splashViewModel.scheduleTimer(callback)

        advanceTimeBy(2000)

        val callbackResult = deferred.await()

        // Assert
        assert(callbackResult)
    }

    @Test
    fun testCheckIfUserLoggedIn_noUser() = testScope.runTest {
        `when`(useCase.checkIfUserLoggedIn()).thenReturn(false)

        val deferred = CompletableDeferred<Boolean>()

        val callback: (Boolean) -> Unit = { result ->
            deferred.complete(result)
        }

        splashViewModel.scheduleTimer(callback)

        advanceTimeBy(2000)

        val callbackResult = deferred.await()
        // Assert
        assert(!callbackResult)
    }
}
