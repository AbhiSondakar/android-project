package com.ecoloop.ui.partner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.repository.PartnerRepository
import com.ecoloop.domain.model.Offer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OffersUiState(
    val offers: List<Offer> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val isAccepting: Boolean = false,
    val acceptingError: String? = null,
    val acceptingSuccess: Boolean = false
)

@HiltViewModel
class OffersViewModel @Inject constructor(
    private val partnerRepository: PartnerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OffersUiState())
    val uiState: StateFlow<OffersUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = OffersUiState(isLoading = true, error = null)
            when (val result = partnerRepository.getOffers()) {
                is ApiResult.Success -> {
                    _uiState.value = OffersUiState(offers = result.data, isLoading = false)
                }
                is ApiResult.Error -> _uiState.value = OffersUiState(
                    isLoading = false, error = result.message
                )
                is ApiResult.Exception -> _uiState.value = OffersUiState(
                    isLoading = false, error = result.exception.message
                )
            }
        }
    }

    fun acceptOffer(offerId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAccepting = true, acceptingError = null, acceptingSuccess = false)
            when (val result = partnerRepository.acceptOffer(offerId)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isAccepting = false,
                        acceptingSuccess = true,
                        offers = _uiState.value.offers.map { o ->
                            if (o.id == offerId) o.copy(status = "accepted") else o
                        }
                    )
                }
                is ApiResult.Error -> _uiState.value = _uiState.value.copy(
                    isAccepting = false, acceptingError = result.message
                )
                is ApiResult.Exception -> _uiState.value = _uiState.value.copy(
                    isAccepting = false, acceptingError = result.exception.message
                )
            }
        }
    }

    fun rejectOffer(offerId: String, reason: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAccepting = true, acceptingError = null, acceptingSuccess = false)
            when (val result = partnerRepository.rejectOffer(offerId, reason)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isAccepting = false,
                        acceptingSuccess = true,
                        offers = _uiState.value.offers.map { o ->
                            if (o.id == offerId) o.copy(status = "rejected") else o
                        }
                    )
                }
                is ApiResult.Error -> _uiState.value = _uiState.value.copy(
                    isAccepting = false, acceptingError = result.message
                )
                is ApiResult.Exception -> _uiState.value = _uiState.value.copy(
                    isAccepting = false, acceptingError = result.exception.message
                )
            }
        }
    }

    fun clearAcceptingStatus() {
        _uiState.value = _uiState.value.copy(acceptingSuccess = false, acceptingError = null)
    }
}
