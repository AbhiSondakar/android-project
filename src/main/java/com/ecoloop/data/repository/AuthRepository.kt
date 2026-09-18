package com.ecoloop.data.repository

import com.ecoloop.data.local.datastore.SessionManager
import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.remote.api.EcoloopApi
import com.ecoloop.data.remote.dto.LoginRequest
import com.ecoloop.data.remote.dto.RegisterRequest
import com.ecoloop.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: EcoloopApi,
    private val sessionManager: SessionManager
) {

    suspend fun login(email: String, password: String): ApiResult<User> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val sessionId = response.headers()["X-Session-Id"]
                if (sessionId != null) {
                    sessionManager.saveSession(sessionId, dto.id, dto.role ?: "HOUSEHOLD")
                }
                ApiResult.success(
                    User(
                        id = dto.id,
                        email = dto.email,
                        name = dto.name,
                        role = dto.role,
                        phone = dto.phone,
                        address = dto.address,
                        pointsBalance = dto.pointsBalance,
                        createdAt = dto.createdAt
                    )
                )
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun register(
        email: String, password: String, name: String,
        phone: String?, address: String?
    ): ApiResult<User> {
        return try {
            val response = api.register(
                RegisterRequest(email, password, name, phone, address)
            )
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val sessionId = response.headers()["X-Session-Id"]
                if (sessionId != null) {
                    sessionManager.saveSession(sessionId, dto.id, dto.role ?: "HOUSEHOLD")
                }
                ApiResult.success(
                    User(
                        id = dto.id,
                        email = dto.email,
                        name = dto.name,
                        role = dto.role,
                        phone = dto.phone,
                        address = dto.address,
                        pointsBalance = dto.pointsBalance,
                        createdAt = dto.createdAt
                    )
                )
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun me(): ApiResult<User> {
        return try {
            val response = api.me()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                ApiResult.success(
                    User(
                        id = dto.id,
                        email = dto.email,
                        name = dto.name,
                        role = dto.role,
                        phone = dto.phone,
                        address = dto.address,
                        pointsBalance = dto.pointsBalance,
                        createdAt = dto.createdAt
                    )
                )
            } else {
                if (response.code() == 401) {
                    sessionManager.clearIfUnauthorized()
                }
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun forgotPassword(email: String): ApiResult<Unit> {
        return try {
            val response = api.forgotPassword(
                com.ecoloop.data.remote.dto.ForgotPasswordRequest(email)
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

    suspend fun logout(): ApiResult<Unit> {
        return try {
            api.logout()
            sessionManager.clearSession()
            ApiResult.success(Unit)
        } catch (e: Exception) {
            sessionManager.clearSession()
            ApiResult.exception(e)
        }
    }

    val isLoggedIn = sessionManager.isLoggedIn
    val userRole = sessionManager.userRole
}
