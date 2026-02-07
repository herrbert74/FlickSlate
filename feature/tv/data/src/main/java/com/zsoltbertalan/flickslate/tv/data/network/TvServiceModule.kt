package com.zsoltbertalan.flickslate.tv.data.network

import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import retrofit2.Retrofit

@ContributesTo(ActivityRetainedScope::class)
interface TvServiceModule {
	companion object {

		@Provides
		@SingleIn(ActivityRetainedScope::class)
		fun provideTvService(retroFit: Retrofit): TvService {
			return retroFit.create(TvService::class.java)
		}

		@Provides
		@SingleIn(ActivityRetainedScope::class)
		fun provideSetTvFavoriteService(retroFit: Retrofit): SetTvFavoriteService {
			return retroFit.create(SetTvFavoriteService::class.java)
		}

	}

}
