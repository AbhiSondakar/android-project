package com.ecoloop.ui.household

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.repository.PickupRepository
import com.ecoloop.domain.model.Pickup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PickupsUiState(
    val pickups: List<Pickup> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val activeSegment: String = "active"
)

@HiltViewModel
class PickupsViewModel @Inject constructor(
    private val pickupRepository: PickupRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PickupsUiState())
    val uiState: StateFlow<PickupsUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = PickupsUiState(isLoading = true)
            when (val result = pickupRepository.getPickups()) {
                is ApiResult.Success -> {
                    val activeList = result.data.filter { p ->
                        p.status != "completed" && p.status != "cancelled"
                    }
                    val historyList = result.data.filter { p ->
                        p.status == "completed" || p.status == "cancelled"
                    }
                    val list = if (_uiState.value.activeSegment == "active") activeList else historyList
                    _uiState.value = _uiState.value.copy(
                        pickups = list,
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

    fun setSegment(segment: String) {
        _uiState.value = _uiState.value.copy(activeSegment = segment)
        refresh()
    }
}
