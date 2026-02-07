package com.zsoltbertalan.flickslate.search.data.network

import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import retrofit2.Retrofit

@ContributesTo(ActivityRetainedScope::class)
interface SearchServiceModule {
	companion object {

		@Provides
		@SingleIn(ActivityRetainedScope::class)
		fun provideSearchService(retroFit: Retrofit): SearchService {
			return retroFit.create(SearchService::class.java)
		}

	}

}
