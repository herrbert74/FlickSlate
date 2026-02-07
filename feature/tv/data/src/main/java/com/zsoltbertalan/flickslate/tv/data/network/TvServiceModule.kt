package com.zsoltbertalan.flickslate.tv.data.network

import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import dagger.Module
import dagger.Provides
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.SingleIn
import retrofit2.Retrofit

@Module
@ContributesTo(ActivityRetainedScope::class)
class TvServiceModule {

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
