package com.zsoltbertalan.flickslate.movies.data.network

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import retrofit2.Retrofit

@ContributesTo(AppScope::class)
interface MoviesServiceModule {
	companion object {

		@Provides
		@SingleIn(AppScope::class)
		internal fun provideMoviesService(retroFit: Retrofit): MoviesService {
			return retroFit.create(MoviesService::class.java)
		}

		@Provides
		@SingleIn(AppScope::class)
		internal fun provideMoviesAccountService(retroFit: Retrofit): SetMovieFavoriteService {
			return retroFit.create(SetMovieFavoriteService::class.java)
		}

	}

}
