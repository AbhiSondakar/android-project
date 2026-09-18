package com.ecoloop.data.repository

import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.remote.api.EcoloopApi
import com.ecoloop.domain.model.Device
import com.ecoloop.data.local.db.DeviceDao
import com.ecoloop.data.local.entity.CachedDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepository @Inject constructor(
    private val api: EcoloopApi,
    private val deviceDao: DeviceDao
) {

    suspend fun getDevices(): ApiResult<List<Device>> {
        return try {
            val response = api.getDevices()
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.map { it.toDomain() })
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun getDevice(id: String): ApiResult<Device> {
        return try {
            val response = api.getDevice(id)
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.toDomain())
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun getCachedDevices(userId: String): Flow<List<Device>> {
        return deviceDao.getAll(userId).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun cacheDevices(userId: String, devices: List<Device>) {
        deviceDao.clearAll(userId)
        deviceDao.insertAll(devices.map { it.toCached() })
    }

    suspend fun createDevice(
        imageFile: File,
        condition: String,
        address: String,
        lat: Double? = null,
        lon: Double? = null
    ): ApiResult<Device> {
        return try {
            val imagePart = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                imageFile.readBytes().toRequestBody("image/*".toMediaType())
            )
            val conditionPart = condition.toRequestBody("text/plain".toMediaType())
            val addressPart = address.toRequestBody("text/plain".toMediaType())
            val latPart = lat?.toString()?.toRequestBody("text/plain".toMediaType())
            val lonPart = lon?.toString()?.toRequestBody("text/plain".toMediaType())

            val response = api.createDevice(imagePart, conditionPart, addressPart, latPart, lonPart)
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.toDomain())
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun cancelPickup(deviceId: String): ApiResult<Unit> {
        return try {
            val response = api.cancelPickup(deviceId)
            if (response.isSuccessful) {
                ApiResult.success(Unit)
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }
}
