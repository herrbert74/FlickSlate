package com.zsoltbertalan.flickslate.shared.data.network

import com.zsoltbertalan.flickslate.movies.data.network.MoviesService
import com.zsoltbertalan.flickslate.search.data.network.SearchService
import com.zsoltbertalan.flickslate.tv.data.network.TvService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkServiceModule {

	@Provides
	@Singleton
	internal fun provideMoviesService(retroFit: Retrofit): MoviesService {
		return retroFit.create(MoviesService::class.java)
	}

	@Provides
	@Singleton
	internal fun provideSearchService(retroFit: Retrofit): SearchService {
		return retroFit.create(SearchService::class.java)
	}

	@Provides
	@Singleton
	internal fun provideTvService(retroFit: Retrofit): TvService {
		return retroFit.create(TvService::class.java)
	}

}
