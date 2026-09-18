package com.ecoloop.data.repository

import com.ecoloop.data.local.db.NotificationDao
import com.ecoloop.data.local.entity.CachedNotification
import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.remote.api.EcoloopApi
import com.ecoloop.domain.model.AppNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val api: EcoloopApi,
    private val notificationDao: NotificationDao
) {

    suspend fun getNotifications(): ApiResult<List<AppNotification>> {
        return try {
            val response = api.getNotifications()
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.map { it.toDomain() })
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun markNotificationRead(id: String): ApiResult<AppNotification> {
        return try {
            val response = api.markNotificationRead(id)
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.toDomain())
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun getCachedNotifications(userId: String): Flow<List<AppNotification>> {
        return notificationDao.getAll(userId).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun cacheNotifications(userId: String, notifications: List<AppNotification>) {
        notificationDao.clearAll(userId)
        notificationDao.insertAll(notifications.map { notif ->
            CachedNotification(
                id = notif.id,
                userId = userId,
                title = notif.title,
                body = notif.body,
                type = notif.type,
                read = notif.read,
                referenceId = notif.referenceId,
                createdAt = notif.createdAt
            )
        })
    }

    suspend fun markReadLocally(userId: String, notificationId: String) {
        val existing = notificationDao.getById(notificationId) ?: return
        notificationDao.update(existing.copy(read = true))
    }
}
