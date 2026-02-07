package com.zsoltbertalan.flickslate.shared.data.network

import com.zsoltbertalan.flickslate.shared.data.BuildConfig
import com.zsoltbertalan.flickslate.shared.domain.di.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

const val BASE_URL: String = "https://api.themoviedb.org/3/"

@ContributesTo(AppScope::class)
interface NetworkModule {
	companion object {

		@Provides
		@SingleIn(AppScope::class)
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
		@SingleIn(AppScope::class)
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

}
