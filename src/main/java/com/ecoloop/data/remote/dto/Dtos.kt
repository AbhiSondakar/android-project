package com.ecoloop.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val phone: String? = null,
    val address: String? = null
)

@Serializable
data class ForgotPasswordRequest(
    val email: String
)

@Serializable
data class UserDto(
    val id: String,
    val email: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val role: String? = null,
    val active: Boolean = true,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("pointsBalance") val pointsBalance: Int = 0
)

@Serializable
data class UserMapResponse(
    val id: String? = null,
    val email: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val role: String? = null,
    @SerialName("pointsBalance") val pointsBalance: Int? = null,
    val organizationName: String? = null,
    val serviceAreas: String? = null,
    val capabilities: String? = null,
    val capacity: Int? = null,
    val licenseUrl: String? = null
)

@Serializable
data class ProfileUpdate(
    val name: String? = null,
    val phone: String? = null,
    val address: String? = null
)

@Serializable
data class PasswordChange(
    @SerialName("currentPassword") val currentPassword: String,
    @SerialName("newPassword") val newPassword: String
)

@Serializable
data class PushTokenRequest(
    val token: String,
    val platform: String
)

@Serializable
data class NotificationPrefs(
    @SerialName("pickupUpdates") val pickupUpdates: Boolean = true,
    @SerialName("pointsUpdates") val pointsUpdates: Boolean = true,
    @SerialName("offerAlerts") val offerAlerts: Boolean = false
)

@Serializable
data class DeviceDto(
    val id: String,
    @SerialName("userId") val userId: String? = null,
    val category: String? = null,
    val condition: String? = null,
    val status: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("aiCategory") val aiCategory: String? = null,
    @SerialName("aiConfidence") val aiConfidence: Double? = null,
    @SerialName("aiStatus") val aiStatus: String? = null,
    @SerialName("aiProvider") val aiProvider: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null
)

@Serializable
data class CreatePickupRequest(
    val deviceId: String,
    val address: String,
    val scheduledAt: String? = null
)

@Serializable
data class PickupDto(
    val id: String,
    @SerialName("userId") val userId: String? = null,
    @SerialName("deviceId") val deviceId: String? = null,
    @SerialName("partnerId") val partnerId: String? = null,
    val status: String? = null,
    val address: String? = null,
    @SerialName("scheduledAt") val scheduledAt: String? = null,
    @SerialName("completedAt") val completedAt: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null
)

@Serializable
data class PartnerDto(
    val id: String,
    @SerialName("userId") val userId: String? = null,
    @SerialName("orgName") val orgName: String? = null,
    val type: String? = null,
    val status: String? = null,
    @SerialName("licenseNo") val licenseNo: String? = null,
    @SerialName("serviceAreas") val serviceAreas: String? = null,
    @SerialName("capabilities") val capabilities: String? = null,
    val capacity: Int? = null,
    val rating: Double? = null,
    @SerialName("activeJobCount") val activeJobCount: Int? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null
)

@Serializable
data class PartnerRegistration(
    val orgName: String,
    val type: String,
    val licenseNo: String? = null,
    val serviceAreas: String? = null,
    val capabilities: String? = null
)

@Serializable
data class PartnerKpi(
    val offersToday: Long = 0,
    val activeJobs: Long = 0,
    @SerialName("monthlyCompletions") val monthlyCompletions: Long = 0,
    @SerialName("pointsBalance") val pointsBalance: Int = 0,
    @SerialName("nextOfferAt") val nextOfferAt: String? = null,
    @SerialName("capacityUsed") val capacityUsed: Long = 0,
    @SerialName("capacityTotal") val capacityTotal: Int = 0
)

@Serializable
data class OfferDto(
    val id: String,
    @SerialName("pickupId") val pickupId: String,
    @SerialName("partnerId") val partnerId: String,
    val status: String = "offered",
    @SerialName("expiresAt") val expiresAt: String? = null,
    @SerialName("createdAt") val createdAt: String? = null
)

@Serializable
data class JobDto(
    val id: String,
    @SerialName("userId") val userId: String? = null,
    @SerialName("deviceId") val deviceId: String? = null,
    @SerialName("partnerId") val partnerId: String? = null,
    val status: String = "pending",
    val address: String? = null,
    @SerialName("scheduledAt") val scheduledAt: String? = null,
    @SerialName("completedAt") val completedAt: String? = null,
    @SerialName("aiCategory") val aiCategory: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null
)

@Serializable
data class VerifyJobRequest(
    @SerialName("aiMatches") val aiMatches: Boolean,
    @SerialName("actualCategory") val actualCategory: String,
    @SerialName("conditionMatches") val conditionMatches: Boolean,
    val notes: String? = null
)

@Serializable
data class RewardLedgerDto(
    val id: String,
    @SerialName("userId") val userId: String,
    val points: Int,
    val type: String,
    val description: String? = null,
    @SerialName("referenceId") val referenceId: String? = null,
    @SerialName("createdAt") val createdAt: String? = null
)

@Serializable
data class RewardCatalogItemDto(
    val id: String,
    val name: String,
    @SerialName("pointsCost") val pointsCost: Int,
    val description: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    val active: Boolean = true,
    @SerialName("createdAt") val createdAt: String? = null
)

@Serializable
data class BalanceResponse(
    @SerialName("userId") val userId: String? = null,
    @SerialName("pointsBalance") val pointsBalance: Int = 0
)

@Serializable
data class RedemptionRequest(
    @SerialName("rewardId") val rewardId: String,
    @SerialName("pointsCost") val pointsCost: Int
)

@Serializable
data class RedemptionResponse(
    val status: String,
    @SerialName("rewardId") val rewardId: String? = null,
    @SerialName("pointsSpent") val pointsSpent: Int? = null
)

@Serializable
data class NotificationDto(
    val id: String,
    @SerialName("userId") val userId: String? = null,
    val title: String,
    val body: String,
    val type: String? = null,
    @SerialName("isRead") val read: Boolean = false,
    @SerialName("referenceId") val referenceId: String? = null,
    @SerialName("createdAt") val createdAt: String? = null
)

@Serializable
data class UnreadCountResponse(
    val count: Int = 0
)

@Serializable
data class ErrorResponse(
    val status: Int? = null,
    val error: String? = null,
    val message: String? = null
)
