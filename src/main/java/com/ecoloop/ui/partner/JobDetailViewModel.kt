package com.ecoloop.ui.partner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.repository.PartnerRepository
import com.ecoloop.domain.model.Job
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class JobDetailUiState(
    val job: Job? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val conditionOk: Boolean = false,
    val evidenceUri: android.net.Uri? = null,
    val notes: String = "",
    val isVerifying: Boolean = false,
    val verifySuccess: Boolean = false
)

@HiltViewModel
class JobDetailViewModel @Inject constructor(
    private val partnerRepository: PartnerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(JobDetailUiState())
    val uiState: StateFlow<JobDetailUiState> = _uiState

    fun load(jobId: String) {
        viewModelScope.launch {
            _uiState.value = JobDetailUiState(isLoading = true, error = null)
            when (val result = partnerRepository.getJob(jobId)) {
                is ApiResult.Success -> {
                    _uiState.value = JobDetailUiState(
                        job = result.data,
                        isLoading = false
                    )
                }
                is ApiResult.Error -> _uiState.value = JobDetailUiState(
                    isLoading = false, error = result.message
                )
                is ApiResult.Exception -> _uiState.value = JobDetailUiState(
                    isLoading = false, error = result.exception.message
                )
            }
        }
    }

    fun setConditionOk(ok: Boolean) {
        _uiState.value = _uiState.value.copy(conditionOk = ok)
    }

    fun setEvidence(uri: android.net.Uri?) {
        _uiState.value = _uiState.value.copy(evidenceUri = uri)
    }

    fun setNotes(notes: String) {
        _uiState.value = _uiState.value.copy(notes = notes)
    }

    fun verifyAndComplete() {
        val state = _uiState.value
        val job = state.job ?: return
        val actualCategory = job.aiCategory ?: "other"

        viewModelScope.launch {
            _uiState.value = state.copy(isVerifying = true, error = null)
            when (val result = partnerRepository.verifyJob(
                jobId = job.id,
                aiMatches = job.aiCategory != null,
                actualCategory = actualCategory,
                conditionMatches = state.conditionOk,
                notes = state.notes.ifEmpty { null }
            )) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isVerifying = false,
                        verifySuccess = true
                    )
                    load(job.id)
                }
                is ApiResult.Error -> _uiState.value = _uiState.value.copy(
                    isVerifying = false, error = result.message
                )
                is ApiResult.Exception -> _uiState.value = _uiState.value.copy(
                    isVerifying = false, error = result.exception.message
                )
            }
        }
    }

    fun clearVerifySuccess() {
        _uiState.value = _uiState.value.copy(verifySuccess = false)
    }
}
