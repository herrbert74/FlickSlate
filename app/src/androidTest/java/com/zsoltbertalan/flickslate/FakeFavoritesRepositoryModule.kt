package com.zsoltbertalan.flickslate

import com.zsoltbertalan.flickslate.account.data.repository.FavoritesRepositoryModule
import com.zsoltbertalan.flickslate.account.domain.api.FavoritesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
	replaces = [FavoritesRepositoryModule::class],
	components = [ActivityRetainedComponent::class]
)
interface FakeFavoritesRepositoryModule {

	@Binds
	fun bindFavoritesRepository(impl: FakeFavoritesRepository): FavoritesRepository

}
