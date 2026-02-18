package com.zsoltbertalan.flickslate.search.ui.main

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.Provider
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import kotlin.reflect.KClass

internal class TestSearchMetroViewModelFactory : MetroViewModelFactory() {

	override val viewModelProviders: Map<KClass<out ViewModel>, Provider<ViewModel>> =
		mapOf(
			SearchViewModel::class to Provider {
				SearchViewModel(
					genreRepository = FakeGenreAccessor(),
					searchRepository = FakeSearchRepository(),
				)
			},
		)

	override val assistedFactoryProviders: Map<KClass<out ViewModel>, Provider<ViewModelAssistedFactory>> = emptyMap()

	override val manualAssistedFactoryProviders:
		Map<KClass<out ManualViewModelAssistedFactory>, Provider<ManualViewModelAssistedFactory>> = emptyMap()
}
