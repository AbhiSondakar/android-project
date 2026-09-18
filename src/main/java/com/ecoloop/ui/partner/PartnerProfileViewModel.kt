package com.ecoloop.ui.partner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.repository.AuthRepository
import com.ecoloop.data.repository.PartnerRepository
import com.ecoloop.domain.model.Partner
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PartnerProfileUiState(
    val partner: Partner? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isSigningOut: Boolean = false,
    val isSaving: Boolean = false,
    val actionSuccess: Boolean = false
)

@HiltViewModel
class PartnerProfileViewModel @Inject constructor(
    private val partnerRepository: PartnerRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PartnerProfileUiState())
    val uiState: StateFlow<PartnerProfileUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = PartnerProfileUiState(isLoading = true, error = null)
            when (val result = partnerRepository.getPartnerProfile()) {
                is ApiResult.Success -> {
                    _uiState.value = PartnerProfileUiState(
                        partner = result.data,
                        isLoading = false
                    )
                }
                is ApiResult.Error -> _uiState.value = PartnerProfileUiState(
                    isLoading = false, error = result.message
                )
                is ApiResult.Exception -> _uiState.value = PartnerProfileUiState(
                    isLoading = false, error = result.exception.message
                )
            }
        }
    }

    fun saveChanges(name: String?, phone: String?, address: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            val body = mutableMapOf<String, String>()
            name?.let { if (it.isNotBlank()) body["orgName"] = it }
            phone?.let { if (it.isNotBlank()) body["phone"] = it }
            address?.let { if (it.isNotBlank()) body["address"] = it }

            if (body.isNotEmpty()) {
                _uiState.value = _uiState.value.copy(isSaving = false, actionSuccess = true)
            } else {
                _uiState.value = _uiState.value.copy(isSaving = false)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSigningOut = true)
            authRepository.logout()
        }
    }

    fun clearActionSuccess() {
        _uiState.value = _uiState.value.copy(actionSuccess = false)
    }
}
