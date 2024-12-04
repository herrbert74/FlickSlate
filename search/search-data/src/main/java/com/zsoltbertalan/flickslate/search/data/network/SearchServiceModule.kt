package com.zsoltbertalan.flickslate.search.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SearchServiceModule {

	@Provides
	@Singleton
	internal fun provideSearchService(retroFit: Retrofit): SearchService {
		return retroFit.create(SearchService::class.java)
	}

}
