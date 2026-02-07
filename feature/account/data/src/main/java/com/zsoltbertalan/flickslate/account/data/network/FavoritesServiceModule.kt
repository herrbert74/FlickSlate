package com.zsoltbertalan.flickslate.account.data.network

import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import dagger.Module
import dagger.Provides
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.SingleIn
import retrofit2.Retrofit

@Module
@ContributesTo(ActivityRetainedScope::class)
class FavoritesServiceModule {

	@Provides
	@SingleIn(ActivityRetainedScope::class)
	fun provideFavoritesService(retrofit: Retrofit): FavoritesService {
		return retrofit.create(FavoritesService::class.java)
	}
}
