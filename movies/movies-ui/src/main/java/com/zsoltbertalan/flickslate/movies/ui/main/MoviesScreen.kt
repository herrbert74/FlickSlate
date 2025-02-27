package com.zsoltbertalan.flickslate.movies.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.zsoltbertalan.flickslate.movies.ui.R
import com.zsoltbertalan.flickslate.shared.model.MovieCardType
import com.zsoltbertalan.flickslate.shared.compose.component.ListTitle
import com.zsoltbertalan.flickslate.shared.compose.component.ShowCard
import com.zsoltbertalan.flickslate.shared.compose.component.paging.FirstPageErrorIndicator
import com.zsoltbertalan.flickslate.shared.compose.component.paging.FirstPageProgressIndicator
import com.zsoltbertalan.flickslate.shared.compose.component.paging.NewPageErrorIndicator
import com.zsoltbertalan.flickslate.shared.compose.component.paging.NewPageProgressIndicator
import com.zsoltbertalan.flickslate.shared.compose.component.paging.PaginatedLazyColumn
import com.zsoltbertalan.flickslate.shared.compose.component.paging.PaginatedLazyRow
import com.zsoltbertalan.flickslate.shared.compose.component.paging.PaginationState
import com.zsoltbertalan.flickslate.shared.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.compose.util.navigate
import com.zsoltbertalan.flickslate.shared.model.Movie

@Composable
fun MoviesScreen(
	popularMoviesPaginatedState: PaginationState<Int, Movie>,
	nowPlayingMoviesPaginatedState: PaginationState<Int, Movie>,
	upcomingMoviesPaginatedState: PaginationState<Int, Movie>,
	modifier: Modifier = Modifier,
	navigateToDetail: (Int) -> Unit,
) {

	PaginatedLazyColumn(
		popularMoviesPaginatedState,
		firstPageProgressIndicator = { FirstPageProgressIndicator() },
		newPageProgressIndicator = { NewPageProgressIndicator() },
		firstPageErrorIndicator = { e ->
			FirstPageErrorIndicator(
				exception = e,
				onRetryClick = {
					popularMoviesPaginatedState.retryLastFailedRequest()
				}
			)
		},
		newPageErrorIndicator = { e ->
			NewPageErrorIndicator(
				exception = e,
				onRetryClick = {
					popularMoviesPaginatedState.retryLastFailedRequest()
				}
			)
		},
		modifier = modifier
			.background(
				brush = Brush.horizontalGradient(
					listOf(Colors.surface, Colors.surfaceDim),
				)
			)
			.fillMaxHeight()
	) {

		item {
			ListTitle(titleId = R.string.upcoming_movies)
		}

		item {
			ShowUpcomingMovies(upcomingMoviesPaginatedState, navigateToDetail)
		}

		item {
			ListTitle(titleId = R.string.now_playing_movies)
		}

		item {
			ShowNowPlayingMovies(nowPlayingMoviesPaginatedState, navigateToDetail)
		}

		item {
			ListTitle(titleId = R.string.popular_movies)
		}

		itemsIndexed(
			popularMoviesPaginatedState.allItems,
		) { _, item ->
			ShowCard(
				modifier = Modifier.navigate(item.id, navigateToDetail),
				title = item.title,
				voteAverage = item.voteAverage,
				overview = item.overview,
				posterPath = item.posterPath,
				cardType = MovieCardType.FULL
			)
		}
	}
}

@Composable
private fun ShowUpcomingMovies(
	paginatedState: PaginationState<Int, Movie>,
	navigateToDetail: (Int) -> Unit,
) {

	PaginatedLazyRow(
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
		modifier = Modifier,
	) {
		itemsIndexed(
			paginatedState.allItems,
		) { _, item ->
			ShowCard(
				modifier = Modifier.navigate(item.id, navigateToDetail),
				title = item.title,
				voteAverage = item.voteAverage,
				overview = item.overview,
				posterPath = item.posterPath,
				cardType = MovieCardType.HALF
			)
		}
	}

}

@Composable
private fun ShowNowPlayingMovies(
	paginatedState: PaginationState<Int, Movie>,
	navigateToDetail: (Int) -> Unit,
) {

	PaginatedLazyRow(
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
		modifier = Modifier,
	) {
		itemsIndexed(
			paginatedState.allItems,
		) { _, item ->
			ShowCard(
				modifier = Modifier.navigate(item.id, navigateToDetail),
				title = item.title,
				voteAverage = item.voteAverage,
				overview = item.overview,
				posterPath = item.posterPath,
				cardType = MovieCardType.HALF
			)
		}
	}

}
