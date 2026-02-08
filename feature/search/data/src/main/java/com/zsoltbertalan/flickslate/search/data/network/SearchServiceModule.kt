package com.zsoltbertalan.flickslate.search.data.network

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import retrofit2.Retrofit

@ContributesTo(AppScope::class)
interface SearchServiceModule {
	companion object {

		@Provides
		@SingleIn(AppScope::class)
		internal fun provideSearchService(retroFit: Retrofit): SearchService {
			return retroFit.create(SearchService::class.java)
		}

	}

}
