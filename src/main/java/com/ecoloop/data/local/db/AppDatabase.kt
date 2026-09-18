package com.ecoloop.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ecoloop.data.local.entity.CachedDevice
import com.ecoloop.data.local.entity.CachedPickup
import com.ecoloop.data.local.entity.CachedNotification
import com.ecoloop.data.local.entity.CachedRewardLedger
import com.ecoloop.data.local.entity.CachedRewardCatalog
import com.ecoloop.data.local.entity.CachedPartner
import com.ecoloop.data.local.entity.CachedOffer
import com.ecoloop.data.local.entity.CachedJob

@Database(
    entities = [
        CachedDevice::class,
        CachedPickup::class,
        CachedNotification::class,
        CachedRewardLedger::class,
        CachedRewardCatalog::class,
        CachedPartner::class,
        CachedOffer::class,
        CachedJob::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
    abstract fun pickupDao(): PickupDao
    abstract fun notificationDao(): NotificationDao
    abstract fun rewardLedgerDao(): RewardLedgerDao
    abstract fun rewardCatalogDao(): RewardCatalogDao
    abstract fun partnerDao(): PartnerDao
    abstract fun offerDao(): OfferDao
    abstract fun jobDao(): JobDao
}
