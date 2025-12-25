package com.zsoltbertalan.flickslate.account.ui.ratings

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
import com.zsoltbertalan.flickslate.account.domain.model.RatedMovie
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvEpisode
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvShow
import com.zsoltbertalan.flickslate.account.ui.R
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.ui.compose.component.ListTitle
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.PaginatedLazyRow
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.PaginationInternalState
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.PaginationState
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.rememberPaginationState
import com.zsoltbertalan.flickslate.shared.ui.navigation.LocalResultStore

@Composable
fun RatingsScreen(
	ratedMovies: PaginationState<Int, RatedMovie>,
	ratedTvShows: PaginationState<Int, RatedTvShow>,
	ratedTvEpisodes: PaginationState<Int, RatedTvEpisode>,
	navigateToMovieDetails: (Int) -> Unit,
	navigateToTvShowDetails: (Int) -> Unit,
	navigateToTvEpisodeDetails: (Int, Int, Int) -> Unit,
	onRefresh: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val resultStore = LocalResultStore.current
	val currentOnRefresh by rememberUpdatedState(onRefresh)

	LaunchedEffect(Unit) {
		val result: Boolean? = resultStore.getResult("RatingChanged")
		if (result == true) {
			currentOnRefresh()
		}
	}

	LazyColumn(
		modifier = modifier
			.fillMaxSize()
			.testTag("RatingsColumn"),
	) {
		item { ListTitle(title = stringResource(id = R.string.rated_movies)) }

		item {
			val ratedMoviesState = ratedMovies.internalState.value
			if (ratedMoviesState is PaginationInternalState.Loaded && ratedMoviesState.items.isEmpty()) {
				EmptyRatedListCard(
					text = stringResource(id = R.string.no_rated_movies),
					modifier = Modifier.padding(horizontal = 16.dp)
				)
			} else {
				PaginatedLazyRow(
					contentPadding = PaddingValues(horizontal = 16.dp),
					horizontalArrangement = Arrangement.spacedBy(16.dp),
					paginationState = ratedMovies
				) {
					items(ratedMovies.allItems) { ratedMovie ->
						RatedShowCard(
							modifier = Modifier.clickable { navigateToMovieDetails(ratedMovie.movie.id) },
							title = ratedMovie.movie.title,
							posterPath = ratedMovie.movie.posterPath,
							rating = ratedMovie.rating
						)
					}
				}
			}
		}

		item {
			ListTitle(
				title = stringResource(id = R.string.rated_tv_shows),
				modifier = Modifier.padding(top = 16.dp)
			)
		}

		item {
			val ratedTvShowsState = ratedTvShows.internalState.value
			if (ratedTvShowsState is PaginationInternalState.Loaded && ratedTvShowsState.items.isEmpty()) {
				EmptyRatedListCard(
					text = stringResource(id = R.string.no_rated_tv_shows),
					modifier = Modifier.padding(horizontal = 16.dp)
				)
			} else {
				PaginatedLazyRow(
					contentPadding = PaddingValues(horizontal = 16.dp),
					horizontalArrangement = Arrangement.spacedBy(16.dp),
					paginationState = ratedTvShows,
				) {
					items(ratedTvShows.allItems) { ratedTvShow ->
						RatedShowCard(
							modifier = Modifier.clickable { navigateToTvShowDetails(ratedTvShow.tvShow.id) },
							title = ratedTvShow.tvShow.name,
							posterPath = ratedTvShow.tvShow.posterPath,
							rating = ratedTvShow.rating
						)
					}
				}
			}
		}

		item {
			ListTitle(
				title = stringResource(id = R.string.rated_tv_episodes),
				modifier = Modifier.padding(top = 16.dp)
			)
		}

		item {
			val ratedTvEpisodesState = ratedTvEpisodes.internalState.value
			if (ratedTvEpisodesState is PaginationInternalState.Loaded && ratedTvEpisodesState.items.isEmpty()) {
				EmptyRatedListCard(
					text = stringResource(id = R.string.no_rated_tv_episodes),
					modifier = Modifier.padding(horizontal = 16.dp)
				)
			} else {
				PaginatedLazyRow(
					contentPadding = PaddingValues(horizontal = 16.dp),
					horizontalArrangement = Arrangement.spacedBy(16.dp),
					paginationState = ratedTvEpisodes,
				) {
					items(ratedTvEpisodes.allItems) { ratedTvEpisode ->
						RatedShowCard(
							modifier = Modifier.clickable {
								navigateToTvEpisodeDetails(
									ratedTvEpisode.tvEpisodeDetail.showId,
									ratedTvEpisode.tvEpisodeDetail.seasonNumber,
									ratedTvEpisode.tvEpisodeDetail.episodeNumber
								)
							},
							title = ratedTvEpisode.tvEpisodeDetail.name ?: "",
							posterPath = ratedTvEpisode.tvEpisodeDetail.stillPath,
							rating = ratedTvEpisode.rating
						)
					}
				}
			}
		}
	}
}

@Composable
@Preview(name = "Empty Lists", showBackground = true)
internal fun RatingsScreenEmptyPreview() {
	RatingsScreen(
		ratedMovies = rememberPaginationState<Int, RatedMovie>(initialPageKey = 1, onRequestPage = {}).apply {
			internalState.value = PaginationInternalState.Loaded(1, 1, 0, emptyList(), true)
		},
		ratedTvShows = rememberPaginationState<Int, RatedTvShow>(initialPageKey = 1, onRequestPage = {}).apply {
			internalState.value = PaginationInternalState.Loaded(1, 1, 0, emptyList(), true)
		},
		ratedTvEpisodes = rememberPaginationState<Int, RatedTvEpisode>(initialPageKey = 1, onRequestPage = {}).apply {
			internalState.value = PaginationInternalState.Loaded(1, 1, 0, emptyList(), true)
		},
		navigateToMovieDetails = {},
		navigateToTvShowDetails = {},
		navigateToTvEpisodeDetails = { _, _, _ -> },
		onRefresh = {}
	)
}

@Composable
@Preview(name = "With Content", showBackground = true)
internal fun RatingsScreenWithContentPreview() {
	val ratedMovie = RatedMovie(
		movie = Movie(
			id = 1,
			title = "Sample Movie",
			voteAverage = 8.5f,
			overview = "Overview",
			posterPath = null
		),
		rating = 9.0f
	)
	val ratedTvShow = RatedTvShow(
		tvShow = TvShow(
			id = 2,
			name = "Sample Show",
			voteAverage = 7.9f,
			overview = "Show Overview",
			posterPath = null
		),
		rating = 8.0f
	)
	val ratedTvEpisode = RatedTvEpisode(
		tvEpisodeDetail = TvEpisodeDetail(
			id = 3,
			showId = 2,
			seasonNumber = 1,
			episodeNumber = 1,
			name = "Episode 1",
			voteAverage = 8.0f,
			overview = "Episode Overview",
			stillPath = null,
			airDate = "2024-01-01",
			voteCount = 1202
		),
		rating = 7.0f
	)
	RatingsScreen(
		ratedMovies = rememberPaginationState<Int, RatedMovie>(initialPageKey = 1, onRequestPage = {}).apply {
			internalState.value = PaginationInternalState.Loaded(1, 1, 1, listOf(ratedMovie), true)
		},
		ratedTvShows = rememberPaginationState<Int, RatedTvShow>(initialPageKey = 1, onRequestPage = {}).apply {
			internalState.value = PaginationInternalState.Loaded(1, 1, 1, listOf(ratedTvShow), true)
		},
		ratedTvEpisodes = rememberPaginationState<Int, RatedTvEpisode>(initialPageKey = 1, onRequestPage = {}).apply {
			internalState.value = PaginationInternalState.Loaded(1, 1, 1, listOf(ratedTvEpisode), true)
		},
		navigateToMovieDetails = {},
		navigateToTvShowDetails = {},
		navigateToTvEpisodeDetails = { _, _, _ -> },
		onRefresh = {}
	)
}
