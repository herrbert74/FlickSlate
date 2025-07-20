package com.zsoltbertalan.flickslate.tv.ui.main

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zsoltbertalan.flickslate.shared.compose.component.ShowCard
import com.zsoltbertalan.flickslate.shared.compose.component.paging.FirstPageErrorIndicator
import com.zsoltbertalan.flickslate.shared.compose.component.paging.FirstPageProgressIndicator
import com.zsoltbertalan.flickslate.shared.compose.component.paging.NewPageErrorIndicator
import com.zsoltbertalan.flickslate.shared.compose.component.paging.NewPageProgressIndicator
import com.zsoltbertalan.flickslate.shared.compose.component.paging.PaginatedLazyColumn
import com.zsoltbertalan.flickslate.shared.compose.component.paging.PaginationState
import com.zsoltbertalan.flickslate.shared.compose.util.navigate
import com.zsoltbertalan.flickslate.shared.model.MovieCardType
import com.zsoltbertalan.flickslate.tv.domain.model.TvShow

@Composable
fun TvScreen(
	paginatedState: PaginationState<Int, TvShow>,
	modifier: Modifier = Modifier,
	popTo: (Int) -> Unit,
) {
	PaginatedLazyColumn(
		paginatedState,
		firstPageProgressIndicator = { FirstPageProgressIndicator() },
		newPageProgressIndicator = { NewPageProgressIndicator() },
		firstPageErrorIndicator = { e ->
			FirstPageErrorIndicator(
				exception = e,
				onRetryClick = {
					paginatedState.retryLastFailedRequest()
				}
			)
		},
		newPageErrorIndicator = { e ->
			NewPageErrorIndicator(
				exception = e,
				onRetryClick = {
					paginatedState.retryLastFailedRequest()
				}
			)
		},
		modifier = modifier
			.fillMaxHeight(),
	) {
		itemsIndexed(
			paginatedState.allItems,
		) { _, item ->
			ShowCard(
				modifier = Modifier.navigate(item.id, popTo),
				title = item.name,
				voteAverage = item.voteAverage,
				overview = item.overview,
				posterPath = item.posterPath,
				cardType = MovieCardType.FULL
			)
		}
	}

}
