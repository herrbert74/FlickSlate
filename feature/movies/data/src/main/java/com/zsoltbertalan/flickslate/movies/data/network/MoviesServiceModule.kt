package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import dagger.Module
import dagger.Provides
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.SingleIn
import retrofit2.Retrofit

@Module
@ContributesTo(ActivityRetainedScope::class)
internal class MoviesServiceModule {

	@Provides
	@SingleIn(ActivityRetainedScope::class)
	internal fun provideMoviesService(retroFit: Retrofit): MoviesService {
		return retroFit.create(MoviesService::class.java)
	}

	@Provides
	@SingleIn(ActivityRetainedScope::class)
	internal fun provideMoviesAccountService(retroFit: Retrofit): SetMovieFavoriteService {
		return retroFit.create(SetMovieFavoriteService::class.java)
	}

}
