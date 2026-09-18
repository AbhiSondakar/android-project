package com.ecoloop.data.repository

import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.remote.api.EcoloopApi
import com.ecoloop.data.remote.dto.CreatePickupRequest
import com.ecoloop.domain.model.Pickup
import com.ecoloop.data.local.db.PickupDao
import com.ecoloop.data.local.entity.CachedPickup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PickupRepository @Inject constructor(
    private val api: EcoloopApi,
    private val pickupDao: PickupDao
) {

    suspend fun getPickups(): ApiResult<List<Pickup>> {
        return try {
            val response = api.getPickups()
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.map { it.toDomain() })
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun getPickup(id: String): ApiResult<Pickup> {
        return try {
            val response = api.getPickup(id)
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.toDomain())
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun createPickup(deviceId: String, address: String, scheduledAt: String? = null): ApiResult<Pickup> {
        return try {
            val response = api.createPickup(
                CreatePickupRequest(deviceId = deviceId, address = address, scheduledAt = scheduledAt)
            )
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.toDomain())
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun getCachedPickups(userId: String): Flow<List<Pickup>> {
        return pickupDao.getAll(userId).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun cachePickups(userId: String, pickups: List<Pickup>) {
        pickupDao.clearAll(userId)
        pickupDao.insertAll(pickups.map { it.toCached() })
    }
}
