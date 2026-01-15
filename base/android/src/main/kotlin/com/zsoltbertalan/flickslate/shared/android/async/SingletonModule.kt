package com.zsoltbertalan.flickslate.shared.android.async

import android.content.Context
import android.net.ConnectivityManager
import com.zsoltbertalan.flickslate.shared.kotlin.async.DefaultDispatcher
import com.zsoltbertalan.flickslate.shared.kotlin.async.IoDispatcher
import com.zsoltbertalan.flickslate.shared.kotlin.async.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

	@DefaultDispatcher
	@Provides
	fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

	@IoDispatcher
	@Provides
	fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

	@MainDispatcher
	@Provides
	fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

	@Provides
	@Singleton
	internal fun provideConnectivityManager(
		@ApplicationContext context: Context
	): ConnectivityManager {
		return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	}

}
