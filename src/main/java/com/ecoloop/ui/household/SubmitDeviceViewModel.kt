package com.ecoloop.ui.household

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.repository.DeviceRepository
import com.ecoloop.data.repository.PickupRepository
import com.ecoloop.domain.model.DeviceCondition
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class SubmitUiState(
    val step: Int = 1,
    val imageUri: Uri? = null,
    val imageSize: Long = 0,
    val imageMime: String? = null,
    val condition: DeviceCondition? = null,
    val address: String = "",
    val lat: Double? = null,
    val lon: Double? = null,
    val locating: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val submittedDeviceId: String? = null
)

@HiltViewModel
class SubmitDeviceViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val pickupRepository: PickupRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubmitUiState())
    val uiState: StateFlow<SubmitUiState> = _uiState

    fun setImage(uri: Uri?, size: Long, mime: String?) {
        _uiState.value = _uiState.value.copy(
            imageUri = uri, imageSize = size, imageMime = mime, error = null
        )
    }

    fun setCondition(condition: DeviceCondition) {
        _uiState.value = _uiState.value.copy(condition = condition)
    }

    fun setAddress(address: String) {
        _uiState.value = _uiState.value.copy(address = address)
    }

    fun setLocation(lat: Double, lon: Double) {
        _uiState.value = _uiState.value.copy(lat = lat, lon = lon)
    }

    fun setLocating(locating: Boolean) {
        _uiState.value = _uiState.value.copy(locating = locating)
    }

    fun nextStep() {
        _uiState.value = _uiState.value.copy(step = _uiState.value.step + 1)
    }

    fun previousStep() {
        _uiState.value = _uiState.value.copy(step = _uiState.value.step - 1)
    }

    fun submit() {
        val state = _uiState.value
        val imageUri = state.imageUri ?: return
        val condition = state.condition ?: return
        val address = state.address.takeIf { it.isNotBlank() } ?: return

        _uiState.value = state.copy(isSubmitting = true, error = null)
        viewModelScope.launch {
            try {
                val file = uriToFile(imageUri)
                val deviceResult = deviceRepository.createDevice(
                    imageFile = file,
                    condition = condition.value,
                    address = address,
                    lat = state.lat,
                    lon = state.lon
                )
                when (deviceResult) {
                    is ApiResult.Success -> {
                        val device = deviceResult.data
                        pickupRepository.createPickup(
                            deviceId = device.id,
                            address = address,
                            scheduledAt = null
                        )
                        _uiState.value = state.copy(
                            isSubmitting = false,
                            submittedDeviceId = device.id
                        )
                    }
                    is ApiResult.Error -> _uiState.value = state.copy(
                        isSubmitting = false,
                        error = deviceResult.message
                    )
                    is ApiResult.Exception -> _uiState.value = state.copy(
                        isSubmitting = false,
                        error = deviceResult.exception.message
                    )
                }
            } catch (e: Exception) {
                _uiState.value = state.copy(isSubmitting = false, error = e.message ?: "Submission failed")
            }
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open input stream")
        val tempFile = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
        tempFile.outputStream().use { inputStream.copyTo(it) }
        inputStream.close()
        return tempFile
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSubmittedDevice() {
        _uiState.value = _uiState.value.copy(submittedDeviceId = null)
    }
}
