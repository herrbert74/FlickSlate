package com.zsoltbertalan.flickslate.shared.compose.component.paging

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Copied from: [https://github.com/Ahmad-Hamwi/lazy-pagination-compose]
 */
@Composable
fun <KEY, T> PaginatedLazyRow(
	paginationState: PaginationState<KEY, T>,
	modifier: Modifier = Modifier,
	firstPageProgressIndicator: @Composable () -> Unit = {},
	newPageProgressIndicator: @Composable () -> Unit = {},
	firstPageErrorIndicator: @Composable (e: Exception) -> Unit = {},
	newPageErrorIndicator: @Composable (e: Exception) -> Unit = {},
	state: LazyListState = rememberLazyListState(),
	contentPadding: PaddingValues = PaddingValues(0.dp),
	reverseLayout: Boolean = false,
	horizontalArrangement: Arrangement.Horizontal = if (!reverseLayout) Arrangement.Start else Arrangement.End,
	verticalAlignment: Alignment.Vertical = Alignment.Top,
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
		LazyRow(
			modifier = modifier,
			state = state,
			contentPadding = contentPadding,
			reverseLayout = reverseLayout,
			verticalAlignment = verticalAlignment,
			horizontalArrangement = horizontalArrangement,
			flingBehavior = flingBehavior,
			userScrollEnabled = userScrollEnabled,
		) {
			paginatedItemsHandler {
				content()
			}
		}
	}
}
