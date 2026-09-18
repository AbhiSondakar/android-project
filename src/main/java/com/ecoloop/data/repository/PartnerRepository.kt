package com.ecoloop.data.repository

import com.ecoloop.data.remote.ApiResult
import com.ecoloop.data.remote.api.EcoloopApi
import com.ecoloop.domain.model.Partner
import com.ecoloop.domain.model.PartnerKpi
import com.ecoloop.domain.model.Offer
import com.ecoloop.domain.model.Job
import com.ecoloop.data.remote.dto.PartnerRegistration
import com.ecoloop.data.local.db.OfferDao
import com.ecoloop.data.local.db.JobDao
import com.ecoloop.data.local.db.PartnerDao
import com.ecoloop.data.local.entity.CachedPartner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PartnerRepository @Inject constructor(
    private val api: EcoloopApi,
    private val partnerDao: PartnerDao,
    private val offerDao: OfferDao,
    private val jobDao: JobDao
) {

    suspend fun getKpis(): ApiResult<PartnerKpi> {
        return try {
            val response = api.getPartnerKpis()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                ApiResult.success(
                    PartnerKpi(
                        offersToday = dto.offersToday,
                        activeJobs = dto.activeJobs,
                        monthlyCompletions = dto.monthlyCompletions,
                        pointsBalance = dto.pointsBalance,
                        nextOfferAt = dto.nextOfferAt,
                        capacityUsed = dto.capacityUsed,
                        capacityTotal = dto.capacityTotal
                    )
                )
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun getOffers(): ApiResult<List<Offer>> {
        return try {
            val response = api.getOffers()
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.map { it.toDomain() })
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun acceptOffer(offerId: String): ApiResult<Offer> {
        return try {
            val response = api.acceptOffer(offerId)
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.toDomain())
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun rejectOffer(offerId: String, reason: String): ApiResult<Offer> {
        return try {
            val response = api.rejectOffer(offerId, mapOf("reason" to reason))
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.toDomain())
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun getJobs(): ApiResult<List<Job>> {
        return try {
            val response = api.getJobs()
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.map { it.toDomain() })
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun getJob(id: String): ApiResult<Job> {
        return try {
            val response = api.getJob(id)
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.toDomain())
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun verifyJob(jobId: String, aiMatches: Boolean, actualCategory: String, conditionMatches: Boolean, notes: String?): ApiResult<Job> {
        return try {
            val response = api.verifyJob(jobId, com.ecoloop.data.remote.dto.VerifyJobRequest(aiMatches, actualCategory, conditionMatches, notes))
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.toDomain())
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun completeJob(jobId: String): ApiResult<Job> {
        return try {
            val response = api.completeJob(jobId)
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.toDomain())
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun getPartnerProfile(): ApiResult<Partner> {
        return try {
            val response = api.getPartnerMe()
            if (response.isSuccessful && response.body() != null) {
                ApiResult.success(response.body()!!.toDomain())
            } else {
                ApiResult.error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.exception(e)
        }
    }

    suspend fun registerPartner(orgName: String, type: String, licenseNo: String?, serviceAreas: String?, capabilities: String?): ApiResult<Partner> {
        return try {
            val response = api.registerPartner(
                PartnerRegistration(orgName, type, licenseNo, serviceAreas, capabilities)
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

    suspend fun getCachedPartner(userId: String): Flow<Partner?> {
        return flowOf(partnerDao.getById(userId)?.toDomain())
    }

    suspend fun cachePartner(partner: Partner) {
        partnerDao.clear(partner.userId ?: return)
        partnerDao.insert(partner.copy(userId = partner.userId ?: "").toCached())
    }

    suspend fun getCachedOffers(partnerId: String): Flow<List<Offer>> =
        offerDao.getAll(partnerId).map { list -> list.map { it.toDomain() } }

    suspend fun cacheOffers(partnerId: String, offers: List<Offer>) {
        offerDao.clearAll(partnerId)
        offerDao.insertAll(offers.map { offer ->
            com.ecoloop.data.local.entity.CachedOffer(
                id = offer.id,
                pickupId = offer.pickupId,
                partnerId = partnerId,
                status = offer.status,
                expiresAt = offer.expiresAt,
                createdAt = offer.createdAt
            )
        })
    }
}
