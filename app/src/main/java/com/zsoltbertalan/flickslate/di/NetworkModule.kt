package com.zsoltbertalan.flickslate.di

import com.zsoltbertalan.flickslate.BASE_URL
import com.zsoltbertalan.flickslate.data.network.FlickSlateService
import com.zsoltbertalan.flickslate.data.repository.GenreAccessor
import com.zsoltbertalan.flickslate.data.repository.MoviesAccessor
import com.zsoltbertalan.flickslate.data.repository.SearchAccessor
import com.zsoltbertalan.flickslate.data.repository.TvAccessor
import com.zsoltbertalan.flickslate.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.domain.api.SearchRepository
import com.zsoltbertalan.flickslate.domain.api.TvRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@Suppress("unused")
@InstallIn(SingletonComponent::class)
class NetworkModule {

	@Provides
	@Singleton
	internal fun provideFlickSlateRetrofit(): Retrofit {
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY

		val httpClient = OkHttpClient.Builder()

		httpClient.addInterceptor(logging)
		return Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.client(httpClient.build())
			.build()
	}

	@Provides
	@Singleton
	internal fun provideFlickSlateService(retroFit: Retrofit): FlickSlateService {
		return retroFit.create(FlickSlateService::class.java)
	}

	@Provides
	@Singleton
	fun provideGenreRepository(flickSlateService: FlickSlateService): GenreRepository =
		GenreAccessor(flickSlateService)

	@Provides
	@Singleton
	fun provideMoviesRepository(flickSlateService: FlickSlateService): MoviesRepository =
		MoviesAccessor(flickSlateService)

	@Provides
	@Singleton
	fun provideSearchRepository(flickSlateService: FlickSlateService): SearchRepository =
		SearchAccessor(flickSlateService)

	@Provides
	@Singleton
	fun provideTvRepository(flickSlateService: FlickSlateService): TvRepository =
		TvAccessor(flickSlateService)

}
