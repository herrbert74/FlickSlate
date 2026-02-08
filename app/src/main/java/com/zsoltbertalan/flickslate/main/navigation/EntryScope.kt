package com.zsoltbertalan.flickslate.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.enableSavedStateHandles
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.compose.LocalSavedStateRegistryOwner
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
fun EntryScope(
	key: Any,
	isInBackstack: () -> Boolean,
	scopedViewModel: NavScopedViewModel = metroViewModel(),
	content: @Composable () -> Unit
) {
	val currentIsInBackstack by rememberUpdatedState(isInBackstack)
	DisposableEffect(key) {
		onDispose {
			if (!currentIsInBackstack()) {
				scopedViewModel.clear(key)
			}
		}
	}

	val viewModelStore = remember(key) { scopedViewModel.getViewModelStore(key) }
	val parentEntry = LocalViewModelStoreOwner.current
	val parentRegistryOwner = LocalSavedStateRegistryOwner.current
	val defaultArgs = (parentEntry as? HasDefaultViewModelProviderFactory)?.defaultViewModelCreationExtras
		?: CreationExtras.Empty

	val owner = remember(viewModelStore, defaultArgs) {
		EntryViewModelStoreOwner(viewModelStore, parentEntry, defaultArgs, parentRegistryOwner)
	}

	CompositionLocalProvider(LocalViewModelStoreOwner provides owner) {
		content()
	}
}

private class EntryViewModelStoreOwner(
	override val viewModelStore: ViewModelStore,
	private val parentEntry: ViewModelStoreOwner?,
	private val defaultArgs: CreationExtras,
	private val parentRegistryOwner: SavedStateRegistryOwner
) : ViewModelStoreOwner, HasDefaultViewModelProviderFactory, SavedStateRegistryOwner by parentRegistryOwner {

	init {
		enableSavedStateHandles()
	}

	override val defaultViewModelProviderFactory: ViewModelProvider.Factory
		get() = (parentEntry as? HasDefaultViewModelProviderFactory)?.defaultViewModelProviderFactory
			?: ViewModelProvider.NewInstanceFactory.instance

	override val defaultViewModelCreationExtras: CreationExtras
		get() = MutableCreationExtras(defaultArgs).apply {
			set(androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY, parentRegistryOwner)
			set(androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY, this@EntryViewModelStoreOwner)
		}
}
