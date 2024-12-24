package com.zsoltbertalan.flickslate.account.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
class MoviesServiceModule {

	@Provides
	@ActivityRetainedScoped
	internal fun provideMoviesService(retroFit: Retrofit): AccountService {
		return retroFit.create(AccountService::class.java)
	}

}
