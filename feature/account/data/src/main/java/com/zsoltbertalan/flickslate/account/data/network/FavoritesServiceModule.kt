package com.zsoltbertalan.flickslate.account.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
internal class FavoritesServiceModule {

	@Provides
	@ActivityRetainedScoped
	fun provideFavoritesService(retrofit: Retrofit): FavoritesService {
		return retrofit.create(FavoritesService::class.java)
	}
}
