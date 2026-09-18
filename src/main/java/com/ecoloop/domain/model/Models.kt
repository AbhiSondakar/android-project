package com.ecoloop.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed class UserRole(val value: String) {
    object Household : UserRole("HOUSEHOLD")
    object Partner : UserRole("PARTNER")
    object Admin : UserRole("ADMIN")

    companion object {
        fun from(role: String?): UserRole = when (role) {
            "PARTNER" -> Partner
            "ADMIN" -> Admin
            else -> Household
        }
    }
}

@Serializable
data class User(
    val id: String,
    val email: String?,
    val name: String?,
    val role: String?,
    val phone: String?,
    val address: String?,
    val pointsBalance: Int?,
    val createdAt: String? = null
) {
    val isPartner: Boolean get() = role == "PARTNER"
    val isHousehold: Boolean get() = role == "HOUSEHOLD"
}

@Serializable
data class Profile(
    val id: String?,
    val email: String?,
    val name: String?,
    val role: String?,
    val phone: String?,
    val address: String?,
    val pointsBalance: Int?,
    val organizationName: String? = null,
    val serviceAreas: String? = null,
    val capabilities: String? = null,
    val capacity: Int? = null,
    val licenseUrl: String? = null
)

@Serializable
data class DeviceCondition(val value: String) {
    companion object {
        val Working = DeviceCondition("working")
        val SlightlyDamaged = DeviceCondition("slightly_damaged")
        val FullyDamaged = DeviceCondition("fully_damaged")
        val All = listOf(Working, SlightlyDamaged, FullyDamaged)
    }
}

@Serializable
data class Device(
    val id: String,
    val userId: String? = null,
    val category: String? = null,
    val condition: String? = null,
    val status: String? = null,
    val imageUrl: String? = null,
    val aiCategory: String? = null,
    val aiConfidence: Double? = null,
    val aiStatus: String? = null,
    val aiProvider: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
) {
    val displayCategory: String get() = category ?: aiCategory ?: "Device"
}

@Serializable
data class Pickup(
    val id: String,
    val userId: String? = null,
    val deviceId: String? = null,
    val partnerId: String? = null,
    val status: String? = null,
    val address: String? = null,
    val scheduledAt: String? = null,
    val completedAt: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

@Serializable
data class Offer(
    val id: String,
    val pickupId: String,
    val partnerId: String,
    val status: String = "offered",
    val expiresAt: String? = null,
    val createdAt: String? = null
)

@Serializable
data class Job(
    val id: String,
    val userId: String? = null,
    val deviceId: String? = null,
    val partnerId: String? = null,
    val status: String = "pending",
    val address: String? = null,
    val scheduledAt: String? = null,
    val completedAt: String? = null,
    val aiCategory: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

@Serializable
data class PartnerKpi(
    val offersToday: Long = 0,
    val activeJobs: Long = 0,
    val monthlyCompletions: Long = 0,
    val pointsBalance: Int = 0,
    val nextOfferAt: String? = null,
    val capacityUsed: Long = 0,
    val capacityTotal: Int = 0
)

@Serializable
data class Partner(
    val id: String,
    val userId: String? = null,
    val orgName: String?,
    val type: String? = null,
    val status: String? = null,
    val licenseNo: String? = null,
    val serviceAreas: String? = null,
    val capabilities: String? = null,
    val capacity: Int? = null,
    val rating: Double? = null,
    val activeJobCount: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

@Serializable
data class PointsEntry(
    val id: String,
    val userId: String,
    val points: Int,
    val type: String,
    val description: String? = null,
    val referenceId: String? = null,
    val createdAt: String? = null
)

@Serializable
data class RewardCatalogItem(
    val id: String,
    val name: String,
    val pointsCost: Int,
    val description: String? = null,
    val imageUrl: String? = null,
    val active: Boolean = true,
    val createdAt: String? = null
)

@Serializable
data class AppNotification(
    val id: String,
    val userId: String? = null,
    val title: String,
    val body: String,
    val type: String? = null,
    val read: Boolean = false,
    val referenceId: String? = null,
    val createdAt: String? = null
)

@Serializable
data class NotificationPrefs(
    val pickupUpdates: Boolean = true,
    val pointsUpdates: Boolean = true,
    val offerAlerts: Boolean = false
)
