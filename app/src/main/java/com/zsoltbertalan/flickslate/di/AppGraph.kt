package com.zsoltbertalan.flickslate.di

import android.app.Application
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import com.zsoltbertalan.flickslate.shared.domain.di.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ViewModelGraph

@DependencyGraph(
	scope = AppScope::class,
	additionalScopes = [ActivityRetainedScope::class],
)
interface AppGraph : ViewModelGraph {
	@DependencyGraph.Factory
	interface Factory {
		fun create(@Provides application: Application): AppGraph
	}
}
