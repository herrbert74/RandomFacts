package com.pelagohealth.codingchallenge.data.db

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
class InMemoryDatabaseModule {

    @Provides
    @Singleton
    fun provideRealmConfiguration() = RealmConfiguration.Builder(schema = setOf(FactDbo::class))
        .inMemory()
        .build()

    @Provides
    @Singleton
    fun provideRealm(realmConfiguration: RealmConfiguration) = Realm.open(realmConfiguration)

    @Provides
    @Singleton
    internal fun provideFactDataSource(
        realm: Realm
    ): FactDataSource {
        return FactDao(realm)
    }

}
