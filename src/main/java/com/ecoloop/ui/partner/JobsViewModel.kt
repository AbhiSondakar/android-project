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

data class JobsUiState(
    val jobs: List<Job> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class JobsViewModel @Inject constructor(
    private val partnerRepository: PartnerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(JobsUiState())
    val uiState: StateFlow<JobsUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = JobsUiState(isLoading = true, error = null)
            when (val result = partnerRepository.getJobs()) {
                is ApiResult.Success -> {
                    _uiState.value = JobsUiState(jobs = result.data, isLoading = false)
                }
                is ApiResult.Error -> _uiState.value = JobsUiState(
                    isLoading = false, error = result.message
                )
                is ApiResult.Exception -> _uiState.value = JobsUiState(
                    isLoading = false, error = result.exception.message
                )
            }
        }
    }
}
