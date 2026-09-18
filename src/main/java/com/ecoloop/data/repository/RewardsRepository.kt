package com.ecoloop.data.repository

import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.remote.api.EcoloopApi
import com.ecoloop.data.remote.dto.RedemptionRequest
import com.ecoloop.domain.model.PointsEntry
import com.ecoloop.domain.model.RewardCatalogItem
import com.ecoloop.data.local.db.RewardLedgerDao
import com.ecoloop.data.local.db.RewardCatalogDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RewardsRepository @Inject constructor(
    private val api: EcoloopApi,
    private val ledgerDao: RewardLedgerDao,
    private val catalogDao: RewardCatalogDao
) {

    suspend fun getBalance(): ApiResult<Int> {
        return try {
            val response = api.getBalance()
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.pointsBalance)
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun getLedger(): ApiResult<List<PointsEntry>> {
        return try {
            val response = api.getLedger()
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.map { it.toDomain() })
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun getCatalog(): ApiResult<List<RewardCatalogItem>> {
        return try {
            val response = api.getCatalog()
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.map { it.toDomain() })
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun redeemPoints(rewardId: String, pointsCost: Int): ApiResult<RedemptionResult> {
        return try {
            val response = api.redeemPoints(RedemptionRequest(rewardId, pointsCost))
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(RedemptionResult.Success(rewardId))
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun getCachedLedger(userId: String): Flow<List<PointsEntry>> =
        ledgerDao.getAll(userId).map { list -> list.map { it.toDomain() } }

    suspend fun cacheLedger(userId: String, entries: List<PointsEntry>) {
        ledgerDao.clearAll(userId)
        ledgerDao.insertAll(entries.map { it.toCached() })
    }

    suspend fun getCachedCatalog(): Flow<List<RewardCatalogItem>> =
        catalogDao.getAll().map { list -> list.map { it.toDomain() } }

    suspend fun cacheCatalog(items: List<RewardCatalogItem>) {
        catalogDao.clearAll()
        catalogDao.insertAll(items.map { item ->
            com.ecoloop.data.local.entity.CachedRewardCatalog(
                id = item.id,
                name = item.name,
                pointsCost = item.pointsCost,
                description = item.description,
                imageUrl = item.imageUrl,
                active = item.active,
                createdAt = item.createdAt
            )
        })
    }
}

sealed class RedemptionResult {
    data class Success(val rewardId: String) : RedemptionResult()
    data class Failure(val message: String) : RedemptionResult()
}
