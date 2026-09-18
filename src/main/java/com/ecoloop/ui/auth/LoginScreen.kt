package com.ecoloop.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecoloop.mobile.R
import com.ecoloop.ui.AuthActionResult
import com.ecoloop.ui.AuthViewModel
import com.ecoloop.ui.components.EcoloopButton
import com.ecoloop.ui.components.EcoloopErrorBanner
import com.ecoloop.ui.components.EcoloopTextField

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val viewModel: AuthViewModel = hiltViewModel()
    val loginState = viewModel.loginState.collectAsState()

    LaunchedEffect(loginState.value) {
        if (loginState.value is AuthActionResult.Success) {
            onLoginSuccess()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "ECOLOOP",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = -0.9.sp
            )
            Text(
                text = "Household e-waste pickup",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            val state = loginState.value
            if (state is AuthActionResult.Error) {
                EcoloopErrorBanner(
                    message = state.message,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            EcoloopTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "you@example.com",
                singleLine = true,
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            EcoloopTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "Enter your password",
                isPassword = true,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            EcoloopButton(
                text = if (state is AuthActionResult.Loading) "Signing in..." else "Sign in",
                onClick = { viewModel.login(email, password) },
                enabled = state !is AuthActionResult.Loading && email.isNotBlank() && password.isNotBlank(),
                isLoading = state is AuthActionResult.Loading,
                fullWidth = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            androidx.compose.material3.TextButton(
                onClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Text(
                    text = "Don't have an account? Sign up",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            androidx.compose.material3.TextButton(
                onClick = onForgotPasswordClick,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Text(
                    text = "Forgot password?",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
