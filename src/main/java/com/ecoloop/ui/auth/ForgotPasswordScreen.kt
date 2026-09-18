package com.ecoloop.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecoloop.ui.AuthActionResult
import com.ecoloop.ui.AuthViewModel
import com.ecoloop.ui.components.EcoloopButton
import com.ecoloop.ui.components.EcoloopErrorBanner
import com.ecoloop.ui.components.EcoloopTextField

@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit,
    onResetSent: () -> Unit
) {
    val viewModel: AuthViewModel = hiltViewModel()
    val loginState = viewModel.loginState.collectAsState()

    LaunchedEffect(loginState.value) {
        if (loginState.value is AuthActionResult.Success) {
            onResetSent()
        }
    }

    var email by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }
    val state = loginState.value

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Back to sign in",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "Reset password",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = -0.9.sp
            )
            Text(
                text = "Enter your email and we'll send a reset link",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (state is AuthActionResult.Error && !submitted) {
                EcoloopErrorBanner(
                    message = state.message,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            if (submitted) {
                Text(
                    text = "If that email is registered, a reset link is on the way.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                EcoloopButton(
                    text = "Back to sign in",
                    onClick = onResetSent,
                    fullWidth = true
                )
            } else {
                EcoloopTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    placeholder = "you@example.com",
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Email
                )
                Spacer(modifier = Modifier.height(24.dp))
                EcoloopButton(
                    text = if (state is AuthActionResult.Loading) "Sending..." else "Send reset link",
                    onClick = {
                        submitted = true
                        viewModel.forgotPassword(email.trim())
                    },
                    enabled = email.isNotBlank() && state !is AuthActionResult.Loading,
                    isLoading = state is AuthActionResult.Loading,
                    fullWidth = true
                )
            }
        }
    }
}
