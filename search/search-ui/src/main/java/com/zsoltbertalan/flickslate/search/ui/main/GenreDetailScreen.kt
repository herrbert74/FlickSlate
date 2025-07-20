package com.zsoltbertalan.flickslate.search.ui.main

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.zsoltbertalan.flickslate.shared.compose.component.ShowCard
import com.zsoltbertalan.flickslate.shared.compose.component.paging.FirstPageErrorIndicator
import com.zsoltbertalan.flickslate.shared.compose.component.paging.FirstPageProgressIndicator
import com.zsoltbertalan.flickslate.shared.compose.component.paging.NewPageErrorIndicator
import com.zsoltbertalan.flickslate.shared.compose.component.paging.NewPageProgressIndicator
import com.zsoltbertalan.flickslate.shared.compose.component.paging.PaginatedLazyColumn
import com.zsoltbertalan.flickslate.shared.compose.component.paging.PaginationState
import com.zsoltbertalan.flickslate.shared.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.compose.util.navigate
import com.zsoltbertalan.flickslate.shared.model.Movie
import com.zsoltbertalan.flickslate.shared.model.MovieCardType

@Composable
fun GenreDetailScreen(
	setTitle: (String) -> Unit,
	setBackgroundColor: (Color) -> Unit,
	genreMoviesPaginatedState: PaginationState<Int, Movie>,
	genreName: String,
	modifier: Modifier = Modifier,
	popTo: (Int) -> Unit,
) {
	setTitle(genreName)

	val bg = Colors.surface

	// To prevent LambdaParameterInRestartableEffect
	val setLatestBackgroundColor by rememberUpdatedState(setBackgroundColor)

	LaunchedEffect(Colors.surface) {
		setLatestBackgroundColor(bg)
	}

	PaginatedLazyColumn(
		genreMoviesPaginatedState,
		firstPageProgressIndicator = { FirstPageProgressIndicator() },
		newPageProgressIndicator = { NewPageProgressIndicator() },
		firstPageErrorIndicator = { e ->
			FirstPageErrorIndicator(
				exception = e,
				onRetryClick = {
					genreMoviesPaginatedState.retryLastFailedRequest()
				}
			)
		},
		newPageErrorIndicator = { e ->
			NewPageErrorIndicator(
				exception = e,
				onRetryClick = {
					genreMoviesPaginatedState.retryLastFailedRequest()
				}
			)
		},
		modifier = modifier.fillMaxHeight(),
	) {
		itemsIndexed(
			genreMoviesPaginatedState.allItems,
		) { _, item ->
			ShowCard(
				modifier = Modifier.navigate(item.id, popTo),
				title = item.title,
				voteAverage = item.voteAverage,
				overview = item.overview,
				posterPath = item.posterPath,
				cardType = MovieCardType.FULL
			)
		}
	}

}
