package com.zsoltbertalan.flickslate.shared.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember

class ResultStore {
	private val results = mutableMapOf<String, Any>()

	fun setResult(key: String, result: Any) {
		results[key] = result
	}

	@Suppress("UNCHECKED_CAST")
	fun <T> getResult(key: String): T? {
		return results.remove(key) as? T
	}
}

val LocalResultStore = compositionLocalOf<ResultStore> { error("No ResultStore provided") }

@Composable
fun rememberResultStore(): ResultStore {
	return remember { ResultStore() }
}
