package com.ecoloop.ui.household

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.repository.AuthRepository
import com.ecoloop.data.repository.UserRepository
import com.ecoloop.domain.model.NotificationPrefs
import com.ecoloop.domain.model.Profile
import com.ecoloop.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val profile: Profile? = null,
    val prefs: NotificationPrefs? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isSaving: Boolean = false,
    val isChangingPassword: Boolean = false,
    val isSavingPrefs: Boolean = false,
    val actionSuccess: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            loadProfile()
            loadPrefs()
        }
    }

    private suspend fun loadProfile() {
        when (val result = userRepository.getProfile()) {
            is ApiResult.Success -> _uiState.value = _uiState.value.copy(profile = result.data)
            is ApiResult.Error -> _uiState.value = _uiState.value.copy(error = result.message)
            is ApiResult.Exception -> _uiState.value = _uiState.value.copy(error = result.exception.message)
        }
    }

    private suspend fun loadPrefs() {
        when (val result = userRepository.getNotificationPrefs()) {
            is ApiResult.Success -> _uiState.value = _uiState.value.copy(prefs = result.data, isLoading = false)
            is ApiResult.Error -> _uiState.value = _uiState.value.copy(isLoading = false)
            is ApiResult.Exception -> _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun updateProfile(name: String, phone: String?, address: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            when (val result = userRepository.updateProfile(name, phone, address)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(isSaving = false, actionSuccess = true)
                    loadProfile()
                }
                is ApiResult.Error -> _uiState.value = _uiState.value.copy(
                    isSaving = false, error = result.message
                )
                is ApiResult.Exception -> _uiState.value = _uiState.value.copy(
                    isSaving = false, error = result.exception.message
                )
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isChangingPassword = true)
            when (val result = userRepository.changePassword(currentPassword, newPassword)) {
                is ApiResult.Success -> _uiState.value = _uiState.value.copy(
                    isChangingPassword = false, actionSuccess = true
                )
                is ApiResult.Error -> _uiState.value = _uiState.value.copy(
                    isChangingPassword = false, error = result.message
                )
                is ApiResult.Exception -> _uiState.value = _uiState.value.copy(
                    isChangingPassword = false, error = result.exception.message
                )
            }
        }
    }

    fun updatePrefs(prefs: NotificationPrefs) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSavingPrefs = true)
            when (val result = userRepository.updateNotificationPrefs(prefs)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(isSavingPrefs = false, actionSuccess = true)
                    _uiState.value = _uiState.value.copy(prefs = prefs)
                }
                is ApiResult.Error -> _uiState.value = _uiState.value.copy(
                    isSavingPrefs = false, error = result.message
                )
                is ApiResult.Exception -> _uiState.value = _uiState.value.copy(
                    isSavingPrefs = false, error = result.exception.message
                )
            }
        }
    }

    fun togglePref(key: String) {
        val current = _uiState.value.prefs ?: NotificationPrefs()
        val updated = when (key) {
            "pickupUpdates" -> current.copy(pickupUpdates = !current.pickupUpdates)
            "pointsUpdates" -> current.copy(pointsUpdates = !current.pointsUpdates)
            "offerAlerts" -> current.copy(offerAlerts = !current.offerAlerts)
            else -> current
        }
        _uiState.value = _uiState.value.copy(prefs = updated)
        updatePrefs(updated)
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun clearActionSuccess() {
        _uiState.value = _uiState.value.copy(actionSuccess = false)
    }
}
