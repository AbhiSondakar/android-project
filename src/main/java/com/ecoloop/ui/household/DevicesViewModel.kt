package com.ecoloop.ui.household

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.repository.DeviceRepository
import com.ecoloop.domain.model.Device
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DevicesUiState(
    val devices: List<Device> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val activeFilter: String = "all"
)

@HiltViewModel
class DevicesViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DevicesUiState())
    val uiState: StateFlow<DevicesUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = deviceRepository.getDevices()) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        devices = result.data,
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

    fun setFilter(filter: String) {
        _uiState.update { it.copy(activeFilter = filter) }
    }

    val filteredDevices: StateFlow<List<Device>> = _uiState.map { state ->
        val filter = state.activeFilter
        state.devices.filter { device ->
            when (filter) {
                "all" -> true
                "completed" -> device.status == "completed"
                else -> device.status == "pending" || device.status == "in_progress"
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
