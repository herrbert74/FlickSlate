package com.zsoltbertalan.flickslate.tv.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
internal class TvServiceModule {

	@Provides
	@ViewModelScoped
	fun provideTvService(retroFit: Retrofit): TvService {
		return retroFit.create(TvService::class.java)
	}

}
