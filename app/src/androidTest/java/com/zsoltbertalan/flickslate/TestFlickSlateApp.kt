package com.zsoltbertalan.flickslate

import com.zsoltbertalan.flickslate.di.AppGraph
import dev.zacsweers.metro.createDynamicGraphFactory

class TestFlickSlateApp : FlickSlateApp() {

	val testOverrides = TestOverrides()

	override val appGraph: AppGraph by lazy {
		createDynamicGraphFactory<AppGraph.Factory>(testOverrides).create(this)
	}
}
