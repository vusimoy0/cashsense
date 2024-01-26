package ru.resodostudios.cashsense.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.resodostudios.cashsense.core.data.repository.CategoriesRepository
import ru.resodostudios.cashsense.core.data.repository.SubscriptionsRepository
import ru.resodostudios.cashsense.core.data.repository.TransactionsRepository
import ru.resodostudios.cashsense.core.data.repository.UserDataRepository
import ru.resodostudios.cashsense.core.data.repository.WalletsRepository
import ru.resodostudios.cashsense.core.data.repository.offline.OfflineCategoriesRepository
import ru.resodostudios.cashsense.core.data.repository.offline.OfflineSubscriptionsRepository
import ru.resodostudios.cashsense.core.data.repository.offline.OfflineTransactionRepository
import ru.resodostudios.cashsense.core.data.repository.offline.OfflineUserDataRepository
import ru.resodostudios.cashsense.core.data.repository.offline.OfflineWalletsRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindCategoriesRepository(
        offlineCategoriesRepository: OfflineCategoriesRepository
    ): CategoriesRepository

    @Binds
    fun bindTransactionsRepository(
        offlineTransactionsRepository: OfflineTransactionRepository
    ): TransactionsRepository

    @Binds
    fun bindWalletsRepository(
        offlineWalletsRepository: OfflineWalletsRepository
    ): WalletsRepository

    @Binds
    fun bindSubscriptionsRepository(
        offlineSubscriptionsRepository: OfflineSubscriptionsRepository
    ): SubscriptionsRepository

    @Binds
    fun bindUserDataRepository(
        offlineUserDataRepository: OfflineUserDataRepository
    ): UserDataRepository
}