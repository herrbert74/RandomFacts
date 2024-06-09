package com.pelagohealth.codingchallenge.data.network

import com.pelagohealth.codingchallenge.common.testhelper.FactDtoMother
import com.pelagohealth.codingchallenge.data.network.dto.FactDto
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@Suppress("unused")
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
class DataModule {

    @Provides
    @Singleton
    internal fun provideFactApi(): FactApi = object : FactApi {

        var requestCount = 0

        override suspend fun getFact(): FactDto {

            println("request count: $requestCount")
            val text = when (requestCount) {
                0 -> "This is a random fact"
                1 -> "This is another random fact"
                2 -> "This is a very random fact"
                3 -> "This is an absolutely random fact"
                else -> "This is yet another random fact"
            }

            requestCount++
            return FactDtoMother.createDefaultFactDto(text)

        }

    }

}
