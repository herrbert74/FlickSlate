package com.zsoltbertalan.flickslate.search.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class SearchServiceModule {

	@Provides
	@ViewModelScoped
	internal fun provideSearchService(retroFit: Retrofit): SearchService {
		return retroFit.create(SearchService::class.java)
	}

}
