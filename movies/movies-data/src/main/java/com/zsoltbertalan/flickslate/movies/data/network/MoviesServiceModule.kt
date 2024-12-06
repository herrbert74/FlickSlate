package com.zsoltbertalan.flickslate.movies.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
class MoviesServiceModule {

	@Provides
	@ActivityRetainedScoped
	internal fun provideMoviesService(retroFit: Retrofit): MoviesService {
		return retroFit.create(MoviesService::class.java)
	}

}
