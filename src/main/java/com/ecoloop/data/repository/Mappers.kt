package com.ecoloop.data.repository

import com.ecoloop.data.local.entity.CachedDevice
import com.ecoloop.data.local.entity.CachedPickup
import com.ecoloop.data.local.entity.CachedNotification
import com.ecoloop.data.local.entity.CachedRewardLedger
import com.ecoloop.data.local.entity.CachedRewardCatalog
import com.ecoloop.data.local.entity.CachedPartner
import com.ecoloop.data.local.entity.CachedOffer
import com.ecoloop.data.local.entity.CachedJob
import com.ecoloop.data.remote.dto.DeviceDto
import com.ecoloop.data.remote.dto.PickupDto
import com.ecoloop.data.remote.dto.NotificationDto
import com.ecoloop.data.remote.dto.RewardLedgerDto
import com.ecoloop.data.remote.dto.RewardCatalogItemDto
import com.ecoloop.data.remote.dto.PartnerDto
import com.ecoloop.data.remote.dto.OfferDto
import com.ecoloop.data.remote.dto.JobDto
import com.ecoloop.domain.model.Device
import com.ecoloop.domain.model.Pickup
import com.ecoloop.domain.model.AppNotification
import com.ecoloop.domain.model.PointsEntry
import com.ecoloop.domain.model.RewardCatalogItem
import com.ecoloop.domain.model.Partner
import com.ecoloop.domain.model.Offer
import com.ecoloop.domain.model.Job

fun DeviceDto.toDomain(): Device = Device(
    id = id,
    userId = userId,
    category = category,
    condition = condition,
    status = status,
    imageUrl = imageUrl,
    aiCategory = aiCategory,
    aiConfidence = aiConfidence,
    aiStatus = aiStatus,
    aiProvider = aiProvider,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Device.toCached(): CachedDevice = CachedDevice(
    id = id,
    userId = userId,
    category = category,
    condition = condition,
    status = status,
    imageUrl = imageUrl,
    aiCategory = aiCategory,
    aiConfidence = aiConfidence,
    aiStatus = aiStatus,
    aiProvider = aiProvider,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun CachedDevice.toDomain(): Device = Device(
    id = id,
    userId = userId,
    category = category,
    condition = condition,
    status = status,
    imageUrl = imageUrl,
    aiCategory = aiCategory,
    aiConfidence = aiConfidence,
    aiStatus = aiStatus,
    aiProvider = aiProvider,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun PickupDto.toDomain(): Pickup = Pickup(
    id = id,
    userId = userId,
    deviceId = deviceId,
    partnerId = partnerId,
    status = status,
    address = address,
    scheduledAt = scheduledAt,
    completedAt = completedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Pickup.toCached(): CachedPickup = CachedPickup(
    id = id,
    userId = userId,
    deviceId = deviceId,
    partnerId = partnerId,
    status = status,
    address = address,
    scheduledAt = scheduledAt,
    completedAt = completedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun CachedPickup.toDomain(): Pickup = Pickup(
    id = id,
    userId = userId,
    deviceId = deviceId,
    partnerId = partnerId,
    status = status,
    address = address,
    scheduledAt = scheduledAt,
    completedAt = completedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun NotificationDto.toDomain(): AppNotification = AppNotification(
    id = id,
    userId = userId,
    title = title,
    body = body,
    type = type,
    read = read,
    referenceId = referenceId,
    createdAt = createdAt
)

fun NotificationDto.toCached(userId: String): CachedNotification = CachedNotification(
    id = id,
    userId = userId,
    title = title,
    body = body,
    type = type,
    read = read,
    referenceId = referenceId,
    createdAt = createdAt
)

fun CachedNotification.toDomain(): AppNotification = AppNotification(
    id = id,
    userId = userId,
    title = title,
    body = body,
    type = type,
    read = read,
    referenceId = referenceId,
    createdAt = createdAt
)

fun RewardLedgerDto.toDomain(): PointsEntry = PointsEntry(
    id = id,
    userId = userId,
    points = points,
    type = type,
    description = description,
    referenceId = referenceId,
    createdAt = createdAt
)

fun RewardLedgerDto.toCached(): CachedRewardLedger = CachedRewardLedger(
    id = id,
    userId = userId,
    points = points,
    type = type,
    description = description,
    referenceId = referenceId,
    createdAt = createdAt
)

fun CachedRewardLedger.toDomain(): PointsEntry = PointsEntry(
    id = id,
    userId = userId,
    points = points,
    type = type,
    description = description,
    referenceId = referenceId,
    createdAt = createdAt
)

fun RewardCatalogItemDto.toDomain(): RewardCatalogItem = RewardCatalogItem(
    id = id,
    name = name,
    pointsCost = pointsCost,
    description = description,
    imageUrl = imageUrl,
    active = active,
    createdAt = createdAt
)

fun RewardCatalogItemDto.toCached(): CachedRewardCatalog = CachedRewardCatalog(
    id = id,
    name = name,
    pointsCost = pointsCost,
    description = description,
    imageUrl = imageUrl,
    active = active,
    createdAt = createdAt
)

fun CachedRewardCatalog.toDomain(): RewardCatalogItem = RewardCatalogItem(
    id = id,
    name = name,
    pointsCost = pointsCost,
    description = description,
    imageUrl = imageUrl,
    active = active,
    createdAt = createdAt
)

fun PointsEntry.toCached(): CachedRewardLedger = CachedRewardLedger(
    id = id,
    userId = userId,
    points = points,
    type = type,
    description = description,
    referenceId = referenceId,
    createdAt = createdAt
)

fun RewardCatalogItem.toCached(): CachedRewardCatalog = CachedRewardCatalog(
    id = id,
    name = name,
    pointsCost = pointsCost,
    description = description,
    imageUrl = imageUrl,
    active = active,
    createdAt = createdAt
)

fun PartnerDto.toDomain(): Partner = Partner(
    id = id,
    userId = userId,
    orgName = orgName,
    type = type,
    status = status,
    licenseNo = licenseNo,
    serviceAreas = serviceAreas,
    capabilities = capabilities,
    capacity = capacity,
    rating = rating,
    activeJobCount = activeJobCount,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun PartnerDto.toCached(): CachedPartner = CachedPartner(
    id = id,
    userId = userId,
    orgName = orgName,
    type = type,
    status = status,
    licenseNo = licenseNo,
    serviceAreas = serviceAreas,
    capabilities = capabilities,
    capacity = capacity,
    rating = rating,
    activeJobCount = activeJobCount,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun CachedPartner.toDomain(): Partner = Partner(
    id = id,
    userId = userId,
    orgName = orgName,
    type = type,
    status = status,
    licenseNo = licenseNo,
    serviceAreas = serviceAreas,
    capabilities = capabilities,
    capacity = capacity,
    rating = rating,
    activeJobCount = activeJobCount,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Partner.toCached(): CachedPartner = CachedPartner(
    id = id,
    userId = userId,
    orgName = orgName,
    type = type,
    status = status,
    licenseNo = licenseNo,
    serviceAreas = serviceAreas,
    capabilities = capabilities,
    capacity = capacity,
    rating = rating,
    activeJobCount = activeJobCount,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun OfferDto.toDomain(): Offer = Offer(
    id = id,
    pickupId = pickupId,
    partnerId = partnerId,
    status = status,
    expiresAt = expiresAt,
    createdAt = createdAt
)

fun OfferDto.toCached(): CachedOffer = CachedOffer(
    id = id,
    pickupId = pickupId,
    partnerId = partnerId,
    status = status,
    expiresAt = expiresAt,
    createdAt = createdAt
)

fun CachedOffer.toDomain(): Offer = Offer(
    id = id,
    pickupId = pickupId,
    partnerId = partnerId,
    status = status,
    expiresAt = expiresAt,
    createdAt = createdAt
)

fun JobDto.toDomain(): Job = Job(
    id = id,
    userId = userId,
    deviceId = deviceId,
    partnerId = partnerId,
    status = status,
    address = address,
    scheduledAt = scheduledAt,
    completedAt = completedAt,
    aiCategory = aiCategory,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun JobDto.toCached(partnerId: String): CachedJob = CachedJob(
    id = id,
    userId = userId,
    deviceId = deviceId,
    partnerId = partnerId,
    status = status,
    address = address,
    scheduledAt = scheduledAt,
    completedAt = completedAt,
    aiCategory = aiCategory,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun CachedJob.toDomain(): Job = Job(
    id = id,
    userId = userId,
    deviceId = deviceId,
    partnerId = partnerId,
    status = status,
    address = address,
    scheduledAt = scheduledAt,
    completedAt = completedAt,
    aiCategory = aiCategory,
    createdAt = createdAt,
    updatedAt = updatedAt
)
