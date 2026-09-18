package com.ecoloop.ui.household

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.repository.DeviceRepository
import com.ecoloop.data.repository.PickupRepository
import com.ecoloop.domain.model.Device
import com.ecoloop.domain.model.Pickup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DeviceDetailUiState(
    val device: Device? = null,
    val pickup: Pickup? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class DeviceDetailViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val pickupRepository: PickupRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeviceDetailUiState())
    val uiState: StateFlow<DeviceDetailUiState> = _uiState

    fun load(deviceId: String) {
        viewModelScope.launch {
            _uiState.value = DeviceDetailUiState(isLoading = true)
            loadDevice(deviceId)
            loadPickup(deviceId)
        }
    }

    private suspend fun loadDevice(deviceId: String) {
        when (val result = deviceRepository.getDevice(deviceId)) {
            is ApiResult.Success -> {
                _uiState.value = _uiState.value.copy(device = result.data)
            }
            is ApiResult.Error -> _uiState.value = _uiState.value.copy(error = result.message)
            is ApiResult.Exception -> _uiState.value = _uiState.value.copy(error = result.exception.message)
        }
    }

    private suspend fun loadPickup(deviceId: String) {
        when (val result = pickupRepository.getPickups()) {
            is ApiResult.Success -> {
                val pickup = result.data.find { it.deviceId == deviceId }
                _uiState.value = _uiState.value.copy(
                    pickup = pickup,
                    isLoading = false
                )
            }
            is ApiResult.Error -> _uiState.value = _uiState.value.copy(isLoading = false)
            is ApiResult.Exception -> _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun cancelPickup(deviceId: String) {
        viewModelScope.launch {
            when (val result = deviceRepository.cancelPickup(deviceId)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        device = _uiState.value.device?.copy(status = "cancelled"),
                        pickup = _uiState.value.pickup?.copy(status = "cancelled")
                    )
                }
                is ApiResult.Error -> _uiState.value = _uiState.value.copy(error = result.message)
                is ApiResult.Exception -> _uiState.value = _uiState.value.copy(error = result.exception.message)
            }
        }
    }
}
