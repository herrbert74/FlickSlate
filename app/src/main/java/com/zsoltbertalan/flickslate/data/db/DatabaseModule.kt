package com.zsoltbertalan.flickslate.data.db

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideRealmConfiguration() = RealmConfiguration.Builder(
        schema = setOf(GenreDbo::class, EtagDbo::class)
    ).build()

    @Provides
    @Singleton
    fun provideRealm(realmConfiguration: RealmConfiguration) = Realm.open(realmConfiguration)

    @Provides
    @Singleton
    internal fun provideGenreDataSource(
        realm: Realm
    ): GenreDataSource {
        return GenreDao(realm)
    }
}
