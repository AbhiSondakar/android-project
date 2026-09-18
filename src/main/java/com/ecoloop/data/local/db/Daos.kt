package com.ecoloop.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ecoloop.data.local.entity.CachedDevice
import com.ecoloop.data.local.entity.CachedPickup
import com.ecoloop.data.local.entity.CachedNotification
import com.ecoloop.data.local.entity.CachedRewardLedger
import com.ecoloop.data.local.entity.CachedRewardCatalog
import com.ecoloop.data.local.entity.CachedPartner
import com.ecoloop.data.local.entity.CachedOffer
import com.ecoloop.data.local.entity.CachedJob
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Query("SELECT * FROM cached_devices WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAll(userId: String): Flow<List<CachedDevice>>

    @Query("SELECT * FROM cached_devices WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): CachedDevice?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(devices: List<CachedDevice>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(device: CachedDevice)

    @Update
    suspend fun update(device: CachedDevice)

    @Query("DELETE FROM cached_devices WHERE userId = :userId")
    suspend fun clearAll(userId: String)

    @Delete
    suspend fun delete(device: CachedDevice)
}

@Dao
interface PickupDao {
    @Query("SELECT * FROM cached_pickups WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAll(userId: String): Flow<List<CachedPickup>>

    @Query("SELECT * FROM cached_pickups WHERE deviceId = :deviceId LIMIT 1")
    suspend fun getByDeviceId(deviceId: String): CachedPickup?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pickups: List<CachedPickup>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pickup: CachedPickup)

    @Query("DELETE FROM cached_pickups WHERE userId = :userId")
    suspend fun clearAll(userId: String)

    @Delete
    suspend fun delete(pickup: CachedPickup)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM cached_notifications WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAll(userId: String): Flow<List<CachedNotification>>

    @Query("SELECT * FROM cached_notifications WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): CachedNotification?

    @Query("SELECT COUNT(*) FROM cached_notifications WHERE userId = :userId AND read = 0")
    suspend fun unreadCount(userId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notifications: List<CachedNotification>)

    @Update
    suspend fun update(notification: CachedNotification)

    @Query("DELETE FROM cached_notifications WHERE userId = :userId")
    suspend fun clearAll(userId: String)
}

@Dao
interface RewardLedgerDao {
    @Query("SELECT * FROM cached_ledger WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAll(userId: String): Flow<List<CachedRewardLedger>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<CachedRewardLedger>)

    @Query("DELETE FROM cached_ledger WHERE userId = :userId")
    suspend fun clearAll(userId: String)
}

@Dao
interface RewardCatalogDao {
    @Query("SELECT * FROM cached_catalog ORDER BY createdAt DESC")
    fun getAll(): Flow<List<CachedRewardCatalog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CachedRewardCatalog>)

    @Query("DELETE FROM cached_catalog")
    suspend fun clearAll()
}

@Dao
interface PartnerDao {
    @Query("SELECT * FROM cached_partners WHERE userId = :userId LIMIT 1")
    suspend fun getById(userId: String): CachedPartner?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(partner: CachedPartner)

    @Query("DELETE FROM cached_partners WHERE userId = :userId")
    suspend fun clear(userId: String)
}

@Dao
interface OfferDao {
    @Query("SELECT * FROM cached_offers WHERE partnerId = :partnerId ORDER BY createdAt DESC")
    fun getAll(partnerId: String): Flow<List<CachedOffer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(offers: List<CachedOffer>)

    @Query("DELETE FROM cached_offers WHERE partnerId = :partnerId")
    suspend fun clearAll(partnerId: String)
}

@Dao
interface JobDao {
    @Query("SELECT * FROM cached_jobs WHERE partnerId = :partnerId ORDER BY createdAt DESC")
    fun getAll(partnerId: String): Flow<List<CachedJob>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(jobs: List<CachedJob>)

    @Query("DELETE FROM cached_jobs WHERE partnerId = :partnerId")
    suspend fun clearAll(partnerId: String)
}
