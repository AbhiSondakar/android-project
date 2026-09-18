package com.ecoloop.ui.household

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.repository.RewardsRepository
import com.ecoloop.domain.model.PointsEntry
import com.ecoloop.domain.model.RewardCatalogItem
import com.ecoloop.data.repository.RedemptionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PointsUiState(
    val balance: Int = 0,
    val ledger: List<PointsEntry> = emptyList(),
    val catalog: List<RewardCatalogItem> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val isRedeeming: Boolean = false,
    val redeemError: String? = null,
    val redeemSuccess: Boolean = false
)

@HiltViewModel
class PointsViewModel @Inject constructor(
    private val rewardsRepository: RewardsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PointsUiState())
    val uiState: StateFlow<PointsUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            loadBalance()
            loadLedger()
            loadCatalog()
        }
    }

    private suspend fun loadBalance() {
        when (val result = rewardsRepository.getBalance()) {
            is ApiResult.Success -> _uiState.value = _uiState.value.copy(balance = result.data)
            is ApiResult.Error -> _uiState.value = _uiState.value.copy(error = result.message)
            is ApiResult.Exception -> _uiState.value = _uiState.value.copy(error = result.exception.message)
        }
    }

    private suspend fun loadLedger() {
        when (val result = rewardsRepository.getLedger()) {
            is ApiResult.Success -> _uiState.value = _uiState.value.copy(ledger = result.data)
            is ApiResult.Error -> {}
            is ApiResult.Exception -> {}
        }
    }

    private suspend fun loadCatalog() {
        when (val result = rewardsRepository.getCatalog()) {
            is ApiResult.Success -> {
                _uiState.value = _uiState.value.copy(
                    catalog = result.data,
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

    fun redeem(reward: RewardCatalogItem) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRedeeming = true, redeemError = null, redeemSuccess = false)
            when (val result = rewardsRepository.redeemPoints(reward.id, reward.pointsCost)) {
                is ApiResult.Success -> {
                    if (result.data is RedemptionResult.Success) {
                        _uiState.value = _uiState.value.copy(
                            isRedeeming = false,
                            redeemSuccess = true,
                            balance = _uiState.value.balance - reward.pointsCost
                        )
                        refresh()
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isRedeeming = false,
                            redeemError = (result.data as? RedemptionResult.Failure)?.message
                                ?: "Could not redeem points"
                        )
                    }
                }
                is ApiResult.Error -> _uiState.value = _uiState.value.copy(
                    isRedeeming = false,
                    redeemError = result.message
                )
                is ApiResult.Exception -> _uiState.value = _uiState.value.copy(
                    isRedeeming = false,
                    redeemError = result.exception.message
                )
            }
        }
    }

    fun clearRedeemStatus() {
        _uiState.value = _uiState.value.copy(redeemSuccess = false, redeemError = null)
    }
}
