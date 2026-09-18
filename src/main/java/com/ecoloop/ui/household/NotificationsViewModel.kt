package com.ecoloop.ui.household

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.repository.NotificationRepository
import com.ecoloop.domain.model.AppNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationsUiState(
    val notifications: List<AppNotification> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = NotificationsUiState(isLoading = true, error = null)
            when (val result = notificationRepository.getNotifications()) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        notifications = result.data,
                        isLoading = false
                    )
                }
                is ApiResult.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false, error = result.message
                )
                is ApiResult.Exception -> _uiState.value = _uiState.value.copy(
                    isLoading = false, error = result.exception.message
                )
            }
        }
    }

    fun markNotificationRead(id: String) {
        viewModelScope.launch {
            when (val result = notificationRepository.markNotificationRead(id)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        notifications = _uiState.value.notifications.map { n ->
                            if (n.id == id) n.copy(read = true) else n
                        }
                    )
                }
                else -> {}
            }
        }
    }
}
