package com.zsoltbertalan.flickslate.di

import android.app.Application
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ViewModelGraph

@DependencyGraph(
	scope = AppScope::class,
)
interface AppGraph : ViewModelGraph {
	@DependencyGraph.Factory
	interface Factory {
		fun create(@Provides application: Application): AppGraph
	}
}
