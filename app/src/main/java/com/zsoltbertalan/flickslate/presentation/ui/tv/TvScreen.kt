package com.zsoltbertalan.flickslate.presentation.ui.tv

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zsoltbertalan.flickslate.domain.model.MovieCardType
import com.zsoltbertalan.flickslate.domain.model.Tv
import com.zsoltbertalan.flickslate.presentation.component.ShowCard
import com.zsoltbertalan.flickslate.presentation.component.paging.FirstPageErrorIndicator
import com.zsoltbertalan.flickslate.presentation.component.paging.FirstPageProgressIndicator
import com.zsoltbertalan.flickslate.presentation.component.paging.NewPageErrorIndicator
import com.zsoltbertalan.flickslate.presentation.component.paging.NewPageProgressIndicator
import com.zsoltbertalan.flickslate.presentation.component.paging.PaginatedLazyColumn
import com.zsoltbertalan.flickslate.presentation.component.paging.PaginationState
import com.zsoltbertalan.flickslate.presentation.util.navigate

@Composable
fun TvScreen(
	paginatedState: PaginationState<Int, Tv>,
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
