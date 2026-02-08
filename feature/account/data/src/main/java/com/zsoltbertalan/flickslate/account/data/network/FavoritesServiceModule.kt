package com.zsoltbertalan.flickslate.account.data.network

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import retrofit2.Retrofit

@ContributesTo(AppScope::class)
interface FavoritesServiceModule {

	@Provides
	@SingleIn(AppScope::class)
	fun provideFavoritesService(retrofit: Retrofit): FavoritesService {
		return retrofit.create(FavoritesService::class.java)
	}

}
