package com.zsoltbertalan.flickslate.main.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey

@ViewModelKey(NavScopedViewModel::class)
@ContributesIntoMap(AppScope::class)
class NavScopedViewModel @Inject internal constructor() : ViewModel() {

	private val stores = mutableMapOf<Any, ViewModelStore>()

	fun getViewModelStore(key: Any): ViewModelStore {
		return stores.getOrPut(key) { ViewModelStore() }
	}

	fun clear(key: Any) {
		stores.remove(key)?.clear()
	}

	override fun onCleared() {
		stores.values.forEach { it.clear() }
		stores.clear()
	}
}
