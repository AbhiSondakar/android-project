package com.ecoloop.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.repository.AuthRepository
import com.ecoloop.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AuthUiState {
    object Loading : AuthUiState
    object Unauthenticated : AuthUiState
    data class Authenticated(val user: User) : AuthUiState
}

sealed interface AuthActionResult {
    object Idle : AuthActionResult
    object Loading : AuthActionResult
    data class Success(val user: User) : AuthActionResult
    data class Error(val message: String) : AuthActionResult
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val authState = authRepository.isLoggedIn
        .map { loggedIn ->
            if (loggedIn) AuthUiState.Authenticated(
                user = User(
                    id = "", email = null, name = null, role = null,
                    phone = null, address = null, pointsBalance = null
                )
            ) else AuthUiState.Unauthenticated
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AuthUiState.Loading
        )

    private val _loginState = MutableStateFlow<AuthActionResult>(AuthActionResult.Idle)
    val loginState: StateFlow<AuthActionResult> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<AuthActionResult>(AuthActionResult.Idle)
    val registerState: StateFlow<AuthActionResult> = _registerState.asStateFlow()

    val userRole = authRepository.userRole

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthActionResult.Loading
            when (val result = authRepository.login(email, password)) {
                is ApiResult.Success -> _loginState.value = AuthActionResult.Success(result.data)
                is ApiResult.Error -> _loginState.value = AuthActionResult.Error("Login failed: ${result.message}")
                is ApiResult.Exception -> _loginState.value = AuthActionResult.Error("Network error: ${result.exception.message}")
            }
        }
    }

    fun register(email: String, password: String, name: String, phone: String?, address: String?) {
        viewModelScope.launch {
            _registerState.value = AuthActionResult.Loading
            when (val result = authRepository.register(email, password, name, phone, address)) {
                is ApiResult.Success -> _registerState.value = AuthActionResult.Success(result.data)
                is ApiResult.Error -> _registerState.value = AuthActionResult.Error("Registration failed: ${result.message}")
                is ApiResult.Exception -> _registerState.value = AuthActionResult.Error("Network error: ${result.exception.message}")
            }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _loginState.value = AuthActionResult.Loading
            when (val result = authRepository.forgotPassword(email)) {
                is ApiResult.Success -> {
                    _loginState.value = AuthActionResult.Success(
                        User(id = "", email = email, name = null, role = null,
                            phone = null, address = null, pointsBalance = null)
                    )
                }
                is ApiResult.Error -> _loginState.value = AuthActionResult.Error("Failed: ${result.message}")
                is ApiResult.Exception -> _loginState.value = AuthActionResult.Error("Network error")
            }
        }
    }

    fun clearLoginState() {
        _loginState.value = AuthActionResult.Idle
    }

    fun clearRegisterState() {
        _registerState.value = AuthActionResult.Idle
    }

    fun logout() {
        viewModelScope.launch { authRepository.logout() }
    }
}
