package com.zsoltbertalan.flickslate.base.android.async

import android.app.Application
import android.net.ConnectivityManager
import com.zsoltbertalan.flickslate.base.kotlin.async.DefaultDispatcher
import com.zsoltbertalan.flickslate.base.kotlin.async.IoDispatcher
import com.zsoltbertalan.flickslate.base.kotlin.async.MainDispatcher
import com.zsoltbertalan.flickslate.shared.domain.di.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@ContributesTo(AppScope::class)
interface SingletonModule {

	@DefaultDispatcher
	@Provides
	fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

	@IoDispatcher
	@Provides
	fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

	@MainDispatcher
	@Provides
	fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

	companion object {

		@Provides
		@SingleIn(AppScope::class)
		internal fun provideConnectivityManager(
			application: Application
		): ConnectivityManager {
			return application.getSystemService(ConnectivityManager::class.java)
		}

	}

}
