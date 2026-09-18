package com.ecoloop.ui.partner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.repository.PartnerRepository
import com.ecoloop.domain.model.PartnerKpi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PartnerHomeUiState(
    val kpis: PartnerKpi? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class PartnerHomeViewModel @Inject constructor(
    private val partnerRepository: PartnerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PartnerHomeUiState())
    val uiState: StateFlow<PartnerHomeUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = PartnerHomeUiState(isLoading = true, error = null)
            when (val result = partnerRepository.getKpis()) {
                is ApiResult.Success -> {
                    _uiState.value = PartnerHomeUiState(
                        kpis = result.data,
                        isLoading = false
                    )
                }
                is ApiResult.Error -> _uiState.value = PartnerHomeUiState(
                    isLoading = false, error = result.message
                )
                is ApiResult.Exception -> _uiState.value = PartnerHomeUiState(
                    isLoading = false, error = result.exception.message
                )
            }
        }
    }
}
