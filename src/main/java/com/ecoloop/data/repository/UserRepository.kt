package com.ecoloop.data.repository

import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.remote.api.EcoloopApi
import com.ecoloop.domain.model.Profile
import com.ecoloop.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val api: EcoloopApi,
    private val authRepository: AuthRepository
) {

    suspend fun getProfile(): ApiResult<Profile> {
        return try {
            val response = api.getProfile()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                ApiResult.success(
                    Profile(
                        id = dto.id,
                        email = dto.email,
                        name = dto.name,
                        role = dto.role,
                        phone = dto.phone,
                        address = dto.address,
                        pointsBalance = dto.pointsBalance,
                        organizationName = dto.organizationName,
                        serviceAreas = dto.serviceAreas,
                        capabilities = dto.capabilities,
                        capacity = dto.capacity,
                        licenseUrl = dto.licenseUrl
                    )
                )
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun updateProfile(name: String, phone: String?, address: String?): ApiResult<User> {
        return try {
            val response = api.updateProfile(
                com.ecoloop.data.remote.dto.ProfileUpdate(name, phone, address)
            )
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                ApiResult.success(
                    User(
                        id = dto.id ?: "",
                        email = dto.email,
                        name = dto.name,
                        role = dto.role,
                        phone = dto.phone,
                        address = dto.address,
                        pointsBalance = dto.pointsBalance ?: 0,
                        createdAt = null
                    )
                )
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun changePassword(currentPassword: String, newPassword: String): ApiResult<Unit> {
        return try {
            val response = api.changePassword(
                com.ecoloop.data.remote.dto.PasswordChange(currentPassword, newPassword)
            )
            if (response.isSuccessful) {
                ApiResult.success(Unit)
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun registerPushToken(token: String, platform: String): ApiResult<Unit> {
        return try {
            val response = api.registerPushToken(
                com.ecoloop.data.remote.dto.PushTokenRequest(token, platform)
            )
            if (response.isSuccessful) {
                ApiResult.success(Unit)
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun getNotificationPrefs(): ApiResult<com.ecoloop.domain.model.NotificationPrefs> {
        return try {
            val response = api.getNotificationPrefs()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                ApiResult.success(
                    com.ecoloop.domain.model.NotificationPrefs(
                        pickupUpdates = dto.pickupUpdates,
                        pointsUpdates = dto.pointsUpdates,
                        offerAlerts = dto.offerAlerts
                    )
                )
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun updateNotificationPrefs(prefs: com.ecoloop.domain.model.NotificationPrefs): ApiResult<Unit> {
        return try {
            val response = api.updateNotificationPrefs(
                com.ecoloop.data.remote.dto.NotificationPrefs(
                    pickupUpdates = prefs.pickupUpdates,
                    pointsUpdates = prefs.pointsUpdates,
                    offerAlerts = prefs.offerAlerts
                )
            )
            if (response.isSuccessful) {
                ApiResult.success(Unit)
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun uploadLicense(uri: android.net.Uri): ApiResult<String> {
        return try {
            val context = authRepository::class.java.let { _ -> null }
            ApiResult.error(-1, "License upload not available")
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }
}
