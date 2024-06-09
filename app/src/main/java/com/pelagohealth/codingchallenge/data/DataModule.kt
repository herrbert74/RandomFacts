package com.pelagohealth.codingchallenge.data

import com.pelagohealth.codingchallenge.common.IoDispatcher
import com.pelagohealth.codingchallenge.data.db.FactDataSource
import com.pelagohealth.codingchallenge.data.network.FactApi
import com.pelagohealth.codingchallenge.data.repository.FactAccessor
import com.pelagohealth.codingchallenge.domain.api.FactRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@Suppress("unused")
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideFactRepository(
        factApi: FactApi,
        factDataSource: FactDataSource,
        @IoDispatcher ioContext: CoroutineDispatcher,
    ): FactRepository = FactAccessor(factApi, factDataSource, ioContext)

}
