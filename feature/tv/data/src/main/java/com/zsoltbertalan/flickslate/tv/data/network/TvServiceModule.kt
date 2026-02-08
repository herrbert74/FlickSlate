package com.zsoltbertalan.flickslate.tv.data.network

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import retrofit2.Retrofit

@ContributesTo(AppScope::class)
interface TvServiceModule {
	companion object {

		@Provides
		@SingleIn(AppScope::class)
		internal fun provideTvService(retroFit: Retrofit): TvService {
			return retroFit.create(TvService::class.java)
		}

		@Provides
		@SingleIn(AppScope::class)
		internal fun provideSetTvFavoriteService(retroFit: Retrofit): SetTvFavoriteService {
			return retroFit.create(SetTvFavoriteService::class.java)
		}

	}

}
