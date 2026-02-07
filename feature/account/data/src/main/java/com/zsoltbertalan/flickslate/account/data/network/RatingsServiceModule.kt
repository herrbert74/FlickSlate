package com.zsoltbertalan.flickslate.account.data.network

import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import retrofit2.Retrofit

@ContributesTo(ActivityRetainedScope::class)
interface RatingsServiceModule {

	@Provides
	@SingleIn(ActivityRetainedScope::class)
	fun provideRatingsService(retroFit: Retrofit): RatingsService {
		return retroFit.create(RatingsService::class.java)
	}

}
