package com.ecoloop.di

import android.content.Context
import androidx.room.Room
import com.ecoloop.data.local.db.AppDatabase
import com.ecoloop.data.local.db.DeviceDao
import com.ecoloop.data.local.db.PickupDao
import com.ecoloop.data.local.db.NotificationDao
import com.ecoloop.data.local.db.RewardLedgerDao
import com.ecoloop.data.local.db.RewardCatalogDao
import com.ecoloop.data.local.db.PartnerDao
import com.ecoloop.data.local.db.OfferDao
import com.ecoloop.data.local.db.JobDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ecoloop-db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideDeviceDao(db: AppDatabase): DeviceDao = db.deviceDao()

    @Provides
    fun providePickupDao(db: AppDatabase): PickupDao = db.pickupDao()

    @Provides
    fun provideNotificationDao(db: AppDatabase): NotificationDao = db.notificationDao()

    @Provides
    fun provideRewardLedgerDao(db: AppDatabase): RewardLedgerDao = db.rewardLedgerDao()

    @Provides
    fun provideRewardCatalogDao(db: AppDatabase): RewardCatalogDao = db.rewardCatalogDao()

    @Provides
    fun providePartnerDao(db: AppDatabase): PartnerDao = db.partnerDao()

    @Provides
    fun provideOfferDao(db: AppDatabase): OfferDao = db.offerDao()

    @Provides
    fun provideJobDao(db: AppDatabase): JobDao = db.jobDao()
}
