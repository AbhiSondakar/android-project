package com.ecoloop.data.remote.api

import com.ecoloop.data.remote.dto.BalanceResponse
import com.ecoloop.data.remote.dto.CreatePickupRequest
import com.ecoloop.data.remote.dto.DeviceDto
import com.ecoloop.data.remote.dto.ForgotPasswordRequest
import com.ecoloop.data.remote.dto.JobDto
import com.ecoloop.data.remote.dto.LoginRequest
import com.ecoloop.data.remote.dto.NotificationDto
import com.ecoloop.data.remote.dto.NotificationPrefs
import com.ecoloop.data.remote.dto.OfferDto
import com.ecoloop.data.remote.dto.PartnerDto
import com.ecoloop.data.remote.dto.PartnerKpi
import com.ecoloop.data.remote.dto.PartnerRegistration
import com.ecoloop.data.remote.dto.PickupDto
import com.ecoloop.data.remote.dto.PasswordChange
import com.ecoloop.data.remote.dto.ProfileUpdate
import com.ecoloop.data.remote.dto.PushTokenRequest
import com.ecoloop.data.remote.dto.RedemptionRequest
import com.ecoloop.data.remote.dto.RedemptionResponse
import com.ecoloop.data.remote.dto.RegisterRequest
import com.ecoloop.data.remote.dto.RewardCatalogItemDto
import com.ecoloop.data.remote.dto.RewardLedgerDto
import com.ecoloop.data.remote.dto.UnreadCountResponse
import com.ecoloop.data.remote.dto.UserDto
import com.ecoloop.data.remote.dto.UserMapResponse
import com.ecoloop.data.remote.dto.VerifyJobRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface EcoloopApi {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<UserDto>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<UserDto>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<ResponseBody>

    @POST("auth/logout")
    suspend fun logout(): Response<ResponseBody>

    @GET("auth/me")
    suspend fun me(): Response<UserDto>

    @GET("users/me")
    suspend fun getProfile(): Response<UserMapResponse>

    @PATCH("users/me")
    suspend fun updateProfile(@Body body: ProfileUpdate): Response<UserMapResponse>

    @POST("users/me/change-password")
    suspend fun changePassword(@Body body: PasswordChange): Response<ResponseBody>

    @POST("users/me/push-token")
    suspend fun registerPushToken(@Body body: PushTokenRequest): Response<ResponseBody>

    @GET("users/me/notification-prefs")
    suspend fun getNotificationPrefs(): Response<NotificationPrefs>

    @PUT("users/me/notification-prefs")
    suspend fun updateNotificationPrefs(@Body prefs: NotificationPrefs): Response<ResponseBody>

    @Multipart
    @POST("users/me/license")
    suspend fun uploadLicense(@Part file: MultipartBody.Part): Response<ResponseBody>

    @GET("devices")
    suspend fun getDevices(@Query("limit") limit: Int? = null): Response<List<DeviceDto>>

    @GET("devices/{id}")
    suspend fun getDevice(@Path("id") id: String): Response<DeviceDto>

    @Multipart
    @POST("devices")
    suspend fun createDevice(
        @Part image: MultipartBody.Part,
        @Part("condition") condition: RequestBody,
        @Part("address") address: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null,
        @Part("lng") lng: RequestBody? = null
    ): Response<DeviceDto>

    @POST("devices/{id}/cancel-pickup")
    suspend fun cancelPickup(@Path("id") id: String): Response<ResponseBody>

    @GET("pickups")
    suspend fun getPickups(): Response<List<PickupDto>>

    @POST("pickups")
    suspend fun createPickup(@Body body: CreatePickupRequest): Response<PickupDto>

    @GET("pickups/{id}")
    suspend fun getPickup(@Path("id") id: String): Response<PickupDto>

    @POST("partners")
    suspend fun registerPartner(@Body body: PartnerRegistration): Response<PartnerDto>

    @GET("partners/offers")
    suspend fun getOffers(): Response<List<OfferDto>>

    @GET("partners/jobs")
    suspend fun getJobs(): Response<List<JobDto>>

    @GET("partners/kpis")
    suspend fun getPartnerKpis(): Response<PartnerKpi>

    @GET("partners/me")
    suspend fun getPartnerMe(): Response<PartnerDto>

    @PATCH("partners/me")
    suspend fun updatePartnerMe(@Body body: Map<String, @JvmSuppressWildcards String>): Response<PartnerDto>

    @POST("partners/offers/{id}/accept")
    suspend fun acceptOffer(@Path("id") id: String): Response<OfferDto>

    @POST("partners/offers/{id}/reject")
    suspend fun rejectOffer(@Path("id") id: String, @Body reason: Map<String, @JvmSuppressWildcards String>): Response<OfferDto>

    @GET("partners/jobs/{id}")
    suspend fun getJob(@Path("id") id: String): Response<JobDto>

    @POST("partners/jobs/{id}/verify")
    suspend fun verifyJob(@Path("id") id: String, @Body body: VerifyJobRequest): Response<JobDto>

    @POST("partners/jobs/{id}/complete")
    suspend fun completeJob(@Path("id") id: String): Response<JobDto>

    @GET("rewards/balance")
    suspend fun getBalance(): Response<BalanceResponse>

    @GET("rewards/ledger")
    suspend fun getLedger(): Response<List<RewardLedgerDto>>

    @GET("rewards/catalog")
    suspend fun getCatalog(): Response<List<RewardCatalogItemDto>>

    @POST("rewards/redeem")
    suspend fun redeemPoints(@Body body: RedemptionRequest): Response<RedemptionResponse>

    @GET("notifications")
    suspend fun getNotifications(): Response<List<NotificationDto>>

    @PATCH("notifications/{id}/read")
    suspend fun markNotificationRead(@Path("id") id: String): Response<NotificationDto>

    @GET("notifications/unread-count")
    suspend fun getUnreadCount(): Response<UnreadCountResponse>
}
