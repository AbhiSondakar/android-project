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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecoloop.ui.AuthActionResult
import com.ecoloop.ui.AuthViewModel
import com.ecoloop.ui.components.EcoloopButton
import com.ecoloop.ui.components.EcoloopErrorBanner
import com.ecoloop.ui.components.EcoloopTextField

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    val viewModel: AuthViewModel = hiltViewModel()
    val registerState = viewModel.registerState.collectAsState()

    LaunchedEffect(registerState.value) {
        if (registerState.value is AuthActionResult.Success) {
            onRegisterSuccess()
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
                text = "Create account",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = -0.9.sp
            )
            Text(
                text = "Household self-signup",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            val state = registerState.value
            if (state is AuthActionResult.Error) {
                EcoloopErrorBanner(
                    message = state.message,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            var name by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var phone by remember { mutableStateOf("") }
            var address by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            EcoloopTextField(
                value = name,
                onValueChange = { name = it },
                label = "Full name",
                placeholder = "Your name"
            )
            Spacer(modifier = Modifier.height(12.dp))
            EcoloopTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "you@example.com",
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(12.dp))
            EcoloopTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Phone",
                placeholder = "+1 234 567 8900",
                keyboardType = KeyboardType.Phone
            )
            Spacer(modifier = Modifier.height(12.dp))
            EcoloopTextField(
                value = address,
                onValueChange = { address = it },
                label = "Pickup address",
                placeholder = "Street, city, postal code",
                maxLines = 3,
                singleLine = false
            )
            Spacer(modifier = Modifier.height(12.dp))
            EcoloopTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "At least 8 characters",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            EcoloopButton(
                text = if (state is AuthActionResult.Loading) "Creating account..." else "Create account",
                onClick = {
                    viewModel.register(email, password, name, phone.ifEmpty { null }, address.ifEmpty { null })
                },
                enabled = state !is AuthActionResult.Loading &&
                    email.isNotBlank() && password.length >= 8 && name.isNotBlank(),
                isLoading = state is AuthActionResult.Loading,
                fullWidth = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Text(
                    text = "Already have an account? Sign in",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
