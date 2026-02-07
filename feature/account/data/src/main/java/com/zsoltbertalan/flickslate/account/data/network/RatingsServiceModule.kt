package com.zsoltbertalan.flickslate.account.data.network

import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import dagger.Module
import dagger.Provides
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.SingleIn
import retrofit2.Retrofit

@Module
@ContributesTo(ActivityRetainedScope::class)
internal class RatingsServiceModule {

	@Provides
	@SingleIn(ActivityRetainedScope::class)
	fun provideRatingsService(retroFit: Retrofit): RatingsService {
		return retroFit.create(RatingsService::class.java)
	}

}
