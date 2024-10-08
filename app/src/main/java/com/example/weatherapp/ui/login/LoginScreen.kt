package com.example.weatherapp.ui.login

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.R
import com.example.weatherapp.ui.login.viewModel.LoginState
import com.example.weatherapp.ui.login.viewModel.LoginViewModel
import com.example.weatherapp.ui.theme.Dimensions


@Composable
fun LoginScreen(
    context: Context,
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.SIXTEEN_DP),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.login), style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(Dimensions.SIXTEEN_DP))

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimensions.SIXTEEN_DP))

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimensions.SIXTEEN_DP))

        Button(
            onClick = {
                if (Patterns.EMAIL_ADDRESS.matcher(email.value.trim())
                        .matches() && password.value.isNotBlank()
                ) {
                    viewModel.login(email.value, password.value)
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.value.trim()).matches()) {
                    Toast.makeText(context, context.getString(R.string.email_error), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.password_error),
                        Toast.LENGTH_LONG
                    ).show()
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.login))
        }

        Spacer(modifier = Modifier.height(Dimensions.SIXTEEN_DP))

        if (loginState is LoginState.Loading) {
            CircularProgressIndicator()
        }

        if (loginState is LoginState.Success) {
            LaunchedEffect(Unit) {
                onLoginSuccess()
            }
        }

        if (loginState is LoginState.Failure) {
            val errorMessage = (loginState as LoginState.Failure).message
            Text(text = errorMessage, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(Dimensions.SIXTEEN_DP))

        TextButton(onClick = { onNavigateToRegister() }) {
            Text(text = stringResource(R.string.don_t_have_an_account_register))
        }
    }
}
