package com.ecoloop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_devices")
data class CachedDevice(
    @PrimaryKey val id: String,
    val userId: String?,
    val category: String?,
    val condition: String?,
    val status: String?,
    val imageUrl: String?,
    val aiCategory: String?,
    val aiConfidence: Double?,
    val aiStatus: String?,
    val aiProvider: String?,
    val createdAt: String?,
    val updatedAt: String?
)

@Entity(tableName = "cached_pickups")
data class CachedPickup(
    @PrimaryKey val id: String,
    val userId: String?,
    val deviceId: String?,
    val partnerId: String?,
    val status: String?,
    val address: String?,
    val scheduledAt: String?,
    val completedAt: String?,
    val createdAt: String?,
    val updatedAt: String?
)

@Entity(tableName = "cached_notifications")
data class CachedNotification(
    @PrimaryKey val id: String,
    val userId: String?,
    val title: String,
    val body: String,
    val type: String?,
    val read: Boolean,
    val referenceId: String?,
    val createdAt: String?
)

@Entity(tableName = "cached_ledger")
data class CachedRewardLedger(
    @PrimaryKey val id: String,
    val userId: String,
    val points: Int,
    val type: String,
    val description: String?,
    val referenceId: String?,
    val createdAt: String?
)

@Entity(tableName = "cached_catalog")
data class CachedRewardCatalog(
    @PrimaryKey val id: String,
    val name: String,
    val pointsCost: Int,
    val description: String?,
    val imageUrl: String?,
    val active: Boolean,
    val createdAt: String?
)

@Entity(tableName = "cached_partners")
data class CachedPartner(
    @PrimaryKey val id: String,
    val userId: String?,
    val orgName: String?,
    val type: String?,
    val status: String?,
    val licenseNo: String?,
    val serviceAreas: String?,
    val capabilities: String?,
    val capacity: Int?,
    val rating: Double?,
    val activeJobCount: Int?,
    val createdAt: String?,
    val updatedAt: String?
)

@Entity(tableName = "cached_offers")
data class CachedOffer(
    @PrimaryKey val id: String,
    val pickupId: String,
    val partnerId: String,
    val status: String,
    val expiresAt: String?,
    val createdAt: String?
)

@Entity(tableName = "cached_jobs")
data class CachedJob(
    @PrimaryKey val id: String,
    val userId: String?,
    val deviceId: String?,
    val partnerId: String?,
    val status: String,
    val address: String?,
    val scheduledAt: String?,
    val completedAt: String?,
    val aiCategory: String?,
    val createdAt: String?,
    val updatedAt: String?
)
