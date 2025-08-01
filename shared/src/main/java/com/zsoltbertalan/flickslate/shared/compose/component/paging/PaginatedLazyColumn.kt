package com.zsoltbertalan.flickslate.shared.compose.component.paging

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Copied from: [https://github.com/Ahmad-Hamwi/lazy-pagination-compose]
 */
@Composable
fun <KEY, T> PaginatedLazyColumn(
	paginationState: PaginationState<KEY, T>,
	modifier: Modifier = Modifier,
	firstPageProgressIndicator: @Composable () -> Unit = {},
	newPageProgressIndicator: @Composable () -> Unit = {},
	firstPageErrorIndicator: @Composable (e: Exception) -> Unit = {},
	newPageErrorIndicator: @Composable (e: Exception) -> Unit = {},
	state: LazyListState = rememberLazyListState(),
	contentPadding: PaddingValues = PaddingValues(0.dp),
	reverseLayout: Boolean = false,
	verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
	horizontalAlignment: Alignment.Horizontal = Alignment.Start,
	flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
	userScrollEnabled: Boolean = true,
	content: LazyListScope.() -> Unit,
) {
	PaginatedLazyScrollable<KEY, T, LazyListState, LazyListScope>(
		paginationState,
		state,
		firstPageProgressIndicator,
		newPageProgressIndicator,
		firstPageErrorIndicator,
		newPageErrorIndicator,
	) { paginatedItemsHandler ->
		LazyColumn(
			modifier = modifier,
			state = state,
			contentPadding = contentPadding,
			reverseLayout = reverseLayout,
			verticalArrangement = verticalArrangement,
			horizontalAlignment = horizontalAlignment,
			flingBehavior = flingBehavior,
			userScrollEnabled = userScrollEnabled,
		) {
			paginatedItemsHandler {
				content()
			}
		}
	}
}
