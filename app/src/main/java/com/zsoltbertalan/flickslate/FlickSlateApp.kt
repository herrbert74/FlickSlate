package com.zsoltbertalan.flickslate

import android.app.Application
import com.zsoltbertalan.flickslate.di.AppGraph
import dev.zacsweers.metro.createGraphFactory
import timber.log.Timber
import timber.log.Timber.Forest.plant

class FlickSlateApp : Application() {

	val appGraph: AppGraph by lazy { createGraphFactory<AppGraph.Factory>().create(this) }

	override fun onCreate() {
		super.onCreate()
		if (BuildConfig.DEBUG) {
			plant(Timber.DebugTree())
		}
	}

}
