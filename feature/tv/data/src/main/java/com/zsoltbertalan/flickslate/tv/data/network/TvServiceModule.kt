package com.zsoltbertalan.flickslate.tv.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
internal class TvServiceModule {

	@Provides
	@ActivityRetainedScoped
	fun provideTvService(retroFit: Retrofit): TvService {
		return retroFit.create(TvService::class.java)
	}

}
