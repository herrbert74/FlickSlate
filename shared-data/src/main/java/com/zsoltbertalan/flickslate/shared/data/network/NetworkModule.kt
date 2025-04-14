package com.zsoltbertalan.flickslate.shared.data.network

import com.zsoltbertalan.flickslate.shared.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

const val BASE_URL: String = "https://api.themoviedb.org/3/"

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {

	@Provides
	@Singleton
	fun provideFlickSlateRetrofit(authInterceptor: Interceptor): Retrofit {
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY

		val httpClient = OkHttpClient.Builder()

		httpClient
			.addInterceptor(logging)
			.addInterceptor(authInterceptor)
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
	fun provideAuthInterceptor(): Interceptor = Interceptor { chain ->
		val original = chain.request()
		val originalHttpUrl = original.url

		val url =
			originalHttpUrl
				.newBuilder()
				.addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
				.build()

		val request = original.newBuilder().url(url).build()
		chain.proceed(request)
	}

}
