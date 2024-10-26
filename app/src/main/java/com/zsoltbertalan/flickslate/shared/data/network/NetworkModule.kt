package com.zsoltbertalan.flickslate.shared.data.network

import com.zsoltbertalan.flickslate.movies.data.network.MoviesService
import com.zsoltbertalan.flickslate.search.data.network.SearchService
import com.zsoltbertalan.flickslate.tv.data.network.TvService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

const val BASE_URL: String = "https://api.themoviedb.org/3/"

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

	@Provides
	@Singleton
	internal fun provideFlickSlateRetrofit(): Retrofit {
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY

		val httpClient = OkHttpClient.Builder()

		httpClient.addInterceptor(logging)
		val json = Json { ignoreUnknownKeys = true }
		val jsonConverterFactory = json.asConverterFactory("application/json; charset=UTF8".toMediaType())
		return Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(jsonConverterFactory)
			.client(httpClient.build())
			.build()
	}

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
