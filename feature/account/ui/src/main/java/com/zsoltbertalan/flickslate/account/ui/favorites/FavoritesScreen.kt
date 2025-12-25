package com.zsoltbertalan.flickslate.account.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteMovie
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteTvShow
import com.zsoltbertalan.flickslate.account.ui.R
import com.zsoltbertalan.flickslate.account.ui.ratings.EmptyRatedListCard
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.ui.compose.component.ListTitle
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.PaginatedLazyRow
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.PaginationInternalState
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.PaginationState
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.rememberPaginationState
import com.zsoltbertalan.flickslate.shared.ui.navigation.LocalResultStore

@Composable
fun FavoritesScreen(
	favoriteMovies: PaginationState<Int, FavoriteMovie>,
	favoriteTvShows: PaginationState<Int, FavoriteTvShow>,
	navigateToMovieDetails: (Int) -> Unit,
	navigateToTvShowDetails: (Int) -> Unit,
	onRefresh: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val resultStore = LocalResultStore.current
	val currentOnRefresh by rememberUpdatedState(onRefresh)

	LaunchedEffect(Unit) {
		val result: Boolean? = resultStore.getResult("FavoriteChanged")
		if (result == true) {
			currentOnRefresh()
		}
	}

	LazyColumn(
		modifier = modifier
			.fillMaxSize()
			.testTag("FavoritesColumn"),
	) {
		item { ListTitle(title = stringResource(id = R.string.favorite_movies)) }

		item {
			val favoriteMoviesState = favoriteMovies.internalState.value
			if (favoriteMoviesState is PaginationInternalState.Loaded && favoriteMoviesState.items.isEmpty()) {
				EmptyRatedListCard(
					text = stringResource(id = R.string.no_favorite_movies),
					modifier = Modifier.padding(horizontal = 16.dp)
				)
			} else {
				PaginatedLazyRow(
					contentPadding = PaddingValues(horizontal = 16.dp),
					horizontalArrangement = Arrangement.spacedBy(16.dp),
					paginationState = favoriteMovies
				) {
					items(favoriteMovies.allItems) { favoriteMovie ->
						FavoriteShowCard(
							modifier = Modifier.clickable { navigateToMovieDetails(favoriteMovie.movie.id) },
							title = favoriteMovie.movie.title,
							posterPath = favoriteMovie.movie.posterPath,
						)
					}
				}
			}
		}

		item {
			ListTitle(
				title = stringResource(id = R.string.favorite_tv_shows),
				modifier = Modifier.padding(top = 16.dp)
			)
		}

		item {
			val favoriteTvShowsState = favoriteTvShows.internalState.value
			if (favoriteTvShowsState is PaginationInternalState.Loaded && favoriteTvShowsState.items.isEmpty()) {
				EmptyRatedListCard(
					text = stringResource(id = R.string.no_favorite_tv_shows),
					modifier = Modifier.padding(horizontal = 16.dp)
				)
			} else {
				PaginatedLazyRow(
					contentPadding = PaddingValues(horizontal = 16.dp),
					horizontalArrangement = Arrangement.spacedBy(16.dp),
					paginationState = favoriteTvShows,
				) {
					items(favoriteTvShows.allItems) { favoriteTvShow ->
						FavoriteShowCard(
							modifier = Modifier.clickable { navigateToTvShowDetails(favoriteTvShow.tvShow.id) },
							title = favoriteTvShow.tvShow.name,
							posterPath = favoriteTvShow.tvShow.posterPath,
						)
					}
				}
			}
		}
	}
}

@Composable
@Preview(name = "Empty Lists", showBackground = true)
internal fun FavoritesScreenEmptyPreview() {
	FavoritesScreen(
		favoriteMovies = rememberPaginationState<Int, FavoriteMovie>(initialPageKey = 1, onRequestPage = {}).apply {
			internalState.value = PaginationInternalState.Loaded(1, 1, 0, emptyList(), true)
		},
		favoriteTvShows = rememberPaginationState<Int, FavoriteTvShow>(initialPageKey = 1, onRequestPage = {}).apply {
			internalState.value = PaginationInternalState.Loaded(1, 1, 0, emptyList(), true)
		},
		navigateToMovieDetails = {},
		navigateToTvShowDetails = {},
		onRefresh = {},
	)
}

@Composable
@Preview(name = "With Content", showBackground = true)
internal fun FavoritesScreenWithContentPreview() {
	val favoriteMovie = FavoriteMovie(
		movie = Movie(
			id = 1,
			title = "Sample Movie",
			voteAverage = 8.5f,
			overview = "Overview",
			posterPath = null
		)
	)
	val favoriteTvShow = FavoriteTvShow(
		tvShow = TvShow(
			id = 2,
			name = "Sample Show",
			voteAverage = 7.9f,
			overview = "Show Overview",
			posterPath = null
		)
	)
	FavoritesScreen(
		favoriteMovies = rememberPaginationState<Int, FavoriteMovie>(initialPageKey = 1, onRequestPage = {}).apply {
			internalState.value = PaginationInternalState.Loaded(1, 1, 1, listOf(favoriteMovie), true)
		},
		favoriteTvShows = rememberPaginationState<Int, FavoriteTvShow>(initialPageKey = 1, onRequestPage = {}).apply {
			internalState.value = PaginationInternalState.Loaded(1, 1, 1, listOf(favoriteTvShow), true)
		},
		navigateToMovieDetails = {},
		navigateToTvShowDetails = {},
		onRefresh = {},
	)
}
