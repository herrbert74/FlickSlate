package com.zsoltbertalan.flickslate.presentation.ui.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.zsoltbertalan.flickslate.R
import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.domain.model.MovieCardType
import com.zsoltbertalan.flickslate.presentation.component.ListTitle
import com.zsoltbertalan.flickslate.presentation.component.ShowCard
import com.zsoltbertalan.flickslate.presentation.component.ShowLoading
import com.zsoltbertalan.flickslate.presentation.design.Colors
import com.zsoltbertalan.flickslate.presentation.util.navigate

@Composable
fun MoviesScreen(
	popularMovies: LazyPagingItems<Movie>,
	nowPlaying: LazyPagingItems<Movie>,
	upcoming: LazyPagingItems<Movie>,
	modifier: Modifier = Modifier,
	navigateToDetail: (Int) -> Unit,
) {
	LazyColumn(
		modifier = modifier
			.background(
				brush = Brush.horizontalGradient(
					listOf(Colors.surface, Colors.surfaceDim),
				)
			)
			.fillMaxHeight()
	) {
		showUpcomingMovies(upcoming, navigateToDetail)
		showNowPlayingMovies(nowPlaying, navigateToDetail)
		showPopularMovies(popularMovies, navigateToDetail)
	}
}

private fun LazyListScope.showPopularMovies(
	popularMovies: LazyPagingItems<Movie>,
	navigateToDetail: (Int) -> Unit,
) {
	item {
		ListTitle(titleId = R.string.popular_movies)
	}

	items(popularMovies.itemCount) { index ->
		popularMovies[index].let {
			it?.let {
				ShowCard(
					modifier = Modifier.navigate(it.id, navigateToDetail),
					title = it.title,
					voteAverage = it.voteAverage,
					overview = it.overview,
					posterPath = it.posterPath,
					cardType = MovieCardType.FULL,
					isFirst = index == 0
				)
			}

		}
	}

	item {
		Spacer(modifier = Modifier.height(20.dp))
	}

	when {
		popularMovies.loadState.refresh is LoadState.Loading -> item {
			ShowLoading(
				text = stringResource(id = R.string.popular_movies)
			)
		}


		popularMovies.loadState.append is LoadState.Loading -> item {
			ShowLoading(
				text = stringResource(id = R.string.popular_movies)
			)
		}

		popularMovies.loadState.refresh is LoadState.Error -> item {
			val stateError = (popularMovies.loadState.refresh as LoadState.Error)
			Text(text = stateError.error.message ?: "Not loading")
		}

	}
}

private fun LazyListScope.showUpcomingMovies(
	upcomingMovies: LazyPagingItems<Movie>,
	popTo: (Int) -> Unit,
) {
	item {
		ListTitle(titleId = R.string.upcoming_movies)
		LazyRow {
			items(upcomingMovies.itemCount) { index ->
				upcomingMovies[index].let {
					it?.let {
						ShowCard(
							modifier = Modifier.navigate(it.id, popTo),
							title = it.title,
							voteAverage = it.voteAverage,
							overview = it.overview,
							posterPath = it.posterPath,
							cardType = MovieCardType.HALF
						)
					}
				}
			}
		}
	}

	//endregion

	item {
		Spacer(modifier = Modifier.width(20.dp))
	}

	when {
		upcomingMovies.loadState.refresh is LoadState.Loading -> item {
			ShowLoading(
				text = stringResource(id = R.string.upcoming_movies)
			)
		}

		upcomingMovies.loadState.append is LoadState.Loading -> item {
			ShowLoading(
				text = stringResource(id = R.string.upcoming_movies)
			)
		}

		upcomingMovies.loadState.refresh is LoadState.Error -> item {
			val stateError = (upcomingMovies.loadState.refresh as LoadState.Error)
			Text(text = stateError.error.message ?: "Not loading")
		}

	}

}

private fun LazyListScope.showNowPlayingMovies(
	nowPlayingMovies: LazyPagingItems<Movie>,
	popTo: (Int) -> Unit,
) {

	item {
		ListTitle(titleId = R.string.now_playing_movies)
		LazyRow {
			items(nowPlayingMovies.itemCount) { index ->
				nowPlayingMovies[index].let {
					it?.let {
						ShowCard(
							modifier = Modifier.navigate(it.id, popTo),
							title = it.title,
							voteAverage = it.voteAverage,
							overview = it.overview,
							posterPath = it.posterPath,
							cardType = MovieCardType.HALF
						)
					}
				}
			}
		}
	}

	item {
		Spacer(modifier = Modifier.width(20.dp))
	}

	when {
		nowPlayingMovies.loadState.refresh is LoadState.Loading -> item {
			ShowLoading(
				text = stringResource(id = R.string.now_playing_movies)
			)
		}

		nowPlayingMovies.loadState.append is LoadState.Loading -> item {
			ShowLoading(
				text = stringResource(id = R.string.now_playing_movies)
			)
		}


		nowPlayingMovies.loadState.refresh is LoadState.Error -> item {
			val stateError = (nowPlayingMovies.loadState.refresh as LoadState.Error)
			Text(text = stateError.error.message ?: "Not loading")
		}

	}

}
