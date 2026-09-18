package com.ecoloop.ui.household

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.repository.AuthRepository
import com.ecoloop.data.repository.DeviceRepository
import com.ecoloop.data.repository.RewardsRepository
import com.ecoloop.domain.model.Device
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

data class HomeUiState(
    val user: User? = null,
    val pointsBalance: Int = 0,
    val recentDevices: List<Device> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class HouseholdHomeViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val rewardsRepository: RewardsRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val user = authRepository.userRole.map { role ->
        User(
            id = "", email = null, name = null, role = role ?: "HOUSEHOLD",
            phone = null, address = null, pointsBalance = null
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true, error = _uiState.value.error)
            loadPoints()
            loadRecentDevices()
        }
    }

    private suspend fun loadPoints() {
        when (val result = rewardsRepository.getBalance()) {
            is ApiResult.Success -> {
                _uiState.value = _uiState.value.copy(pointsBalance = result.data)
            }
            is ApiResult.Error -> _uiState.value = _uiState.value.copy(error = result.message)
            is ApiResult.Exception -> _uiState.value = _uiState.value.copy(error = result.exception.message)
        }
    }

    private suspend fun loadRecentDevices() {
        when (val result = deviceRepository.getDevices()) {
            is ApiResult.Success -> {
                val recent = result.data.take(3)
                _uiState.value = _uiState.value.copy(
                    recentDevices = recent,
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
