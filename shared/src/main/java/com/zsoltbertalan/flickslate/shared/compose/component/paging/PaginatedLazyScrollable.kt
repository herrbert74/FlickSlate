package com.zsoltbertalan.flickslate.shared.compose.component.paging

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal typealias LazyScrollable<LAZY_SCROLLABLE_SCOPE> =
	@Composable (PaginatedScrollableItemsHandler<LAZY_SCROLLABLE_SCOPE>) -> Unit

internal typealias PaginatedScrollableItemsHandler<LAZY_SCROLLABLE_SCOPE> =
	LAZY_SCROLLABLE_SCOPE.(ClientLoadedScrollableContent<LAZY_SCROLLABLE_SCOPE>) -> Unit

internal typealias ClientLoadedScrollableContent<LAZY_SCROLLABLE_SCOPE> = LAZY_SCROLLABLE_SCOPE.() -> Unit

@Suppress("UNCHECKED_CAST", "CyclomaticComplexMethod")
@Composable
internal fun <KEY, T, LAZY_STATE, LAZY_SCROLLABLE_SCOPE> PaginatedLazyScrollable(
	paginationState: PaginationState<KEY, T>,
	state: LAZY_STATE,
	firstPageProgressIndicator: @Composable () -> Unit = {},
	newPageProgressIndicator: @Composable () -> Unit = {},
	firstPageErrorIndicator: @Composable (e: Exception) -> Unit = {},
	newPageErrorIndicator: @Composable (e: Exception) -> Unit = {},
	concreteLazyList: LazyScrollable<LAZY_SCROLLABLE_SCOPE>,
) {
	var internalState by paginationState.internalState

	LaunchedEffect(internalState) {
		(internalState as? PaginationInternalState.Loading)?.also {
			paginationState.run {
				onRequestPage.invoke(this, it.requestedPageKey)
			}
		}
	}

	if (internalState is PaginationInternalState.Loading && internalState.items == null) {
		firstPageProgressIndicator()
	}

	if (internalState is PaginationInternalState.Error && internalState.items == null) {
		firstPageErrorIndicator(
			(internalState as PaginationInternalState.Error).exception
		)
	}

	if (internalState.items != null) {
		LaunchedEffect(state) {
			val lastVisibleItemIndex: Flow<Int> = when (state) {
				is LazyGridState -> snapshotFlow { state.layoutInfo.visibleItemsInfo.lastOrNull() }
					.map { item -> item?.index ?: Int.MIN_VALUE }

				is LazyListState -> snapshotFlow { state.layoutInfo.visibleItemsInfo.lastOrNull() }
					.map { item -> item?.index ?: Int.MIN_VALUE }

				else -> throw IllegalArgumentException("Unsupported Lazy scrollable state type")
			}

			lastVisibleItemIndex.collect {
				val hasReachedLastItem = it >= (internalState.items?.lastIndex ?: Int.MAX_VALUE)

				val isLastPage =
					(internalState as? PaginationInternalState.Loaded)?.isLastPage != false

				val newlyRequestedPageKey =
					(internalState as? PaginationInternalState.Loaded)?.nextPageKey

				val previouslyRequestedPageKey =
					(internalState as? PaginationInternalState.IHasRequestedPageKey<KEY>)?.requestedPageKey

				if (hasReachedLastItem && !isLastPage) {
					internalState = PaginationInternalState.Loading(
						initialPageKey = internalState.initialPageKey,
						requestedPageKey = newlyRequestedPageKey
							?: previouslyRequestedPageKey
							?: internalState.initialPageKey,
						items = internalState.items
					)
				}
			}
		}

		concreteLazyList { clientContent ->
			val internalStateRef = internalState

			val item: (
				key: Any?,
				content: @Composable LAZY_SCROLLABLE_SCOPE.() -> Unit
			) -> Unit = { key, content ->
				when (this) {
					is LazyListScope -> item(key) { content() }
					is LazyGridScope -> item(key) { content() }
					else -> error("Unsupported Lazy scrollable scope type")
				}
			}

			if (internalStateRef.items != null) {
				clientContent()
			}

			if (internalStateRef is PaginationInternalState.Loading) {
				item(
					LazyScrollableKeys.NEW_PAGE_PROGRESS_INDICATOR_KEY
				) {
					newPageProgressIndicator()
				}
			}

			if (internalStateRef is PaginationInternalState.Error) {
				item(
					LazyScrollableKeys.NEW_PAGE_ERROR_INDICATOR_KEY
				) {
					newPageErrorIndicator(internalStateRef.exception)
				}
			}
		}
	}

	LaunchedEffect(internalState) {
		if (internalState is PaginationInternalState.Initial) {
			internalState = PaginationInternalState.Loading(
				initialPageKey = internalState.initialPageKey,
				requestedPageKey = internalState.initialPageKey,
				items = internalState.items
			)
		}
	}
}
