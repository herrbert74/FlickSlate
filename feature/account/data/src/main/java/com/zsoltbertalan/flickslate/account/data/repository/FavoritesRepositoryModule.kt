package com.zsoltbertalan.flickslate.account.data.repository

import com.zsoltbertalan.flickslate.account.domain.api.FavoritesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import javax.inject.Named

@Module(includes = [InternalFavoritesRepositoryModule::class])
@InstallIn(ActivityRetainedComponent::class)
interface FavoritesRepositoryModule {

	@Binds
	fun bindFavoritesRepository(@Named("Internal") impl: FavoritesRepository): FavoritesRepository
}

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface InternalFavoritesRepositoryModule {

	@Binds
	@Named("Internal")
	fun bindFavoritesRepository(impl: FavoritesAccessor): FavoritesRepository
}
