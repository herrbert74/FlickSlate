package com.zsoltbertalan.flickslate.account.ui.ratings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.ui.compose.component.ShowCard
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun RatingsScreen(
	navigateToMovieDetails: (Int) -> Unit,
	navigateToTvShowDetails: (Int) -> Unit,
	navigateToTvEpisodeDetails: (Int, Int, Int) -> Unit,
	uiState: State<RatingsUiState>,
	modifier: Modifier = Modifier,
) {

	when (val state = uiState.value) {
		is RatingsUiState.Loading -> Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
			CircularProgressIndicator()
		}

		is RatingsUiState.Success ->
			RatingsContent(
				state.ratedMovies,
				state.ratedTvShows,
				state.ratedTvEpisodes,
				navigateToMovieDetails,
				navigateToTvShowDetails,
				navigateToTvEpisodeDetails,
				modifier,
			)

		is RatingsUiState.Error -> Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
			Text(text = state.message)
		}
	}
}

@Composable
private fun RatingsContent(
	ratedMovies: ImmutableList<Movie>,
	ratedTvShows: ImmutableList<TvShow>,
	ratedTvEpisodes: ImmutableList<TvEpisodeDetail>,
	navigateToMovieDetails: (Int) -> Unit,
	navigateToTvShowDetails: (Int) -> Unit,
	navigateToTvEpisodeDetails: (Int, Int, Int) -> Unit,
	modifier: Modifier = Modifier
) {
	LazyColumn(
		modifier = modifier.fillMaxSize(),
		contentPadding = PaddingValues(vertical = 16.dp)
	) {
		items(ratedMovies) { movie ->
			ShowCard(
				modifier = Modifier.clickable { navigateToMovieDetails(movie.id) },
				title = movie.title,
				voteAverage = movie.voteAverage,
				overview = movie.overview,
				posterPath = movie.posterPath
			)
		}
		items(ratedTvShows) { tvShow ->
			ShowCard(
				modifier = Modifier.clickable { navigateToTvShowDetails(tvShow.id) },
				title = tvShow.name,
				voteAverage = tvShow.voteAverage,
				overview = tvShow.overview,
				posterPath = tvShow.posterPath
			)
		}
		items(ratedTvEpisodes) { episode ->
			ShowCard(
				modifier = Modifier.clickable {
					navigateToTvEpisodeDetails(episode.showId, episode.seasonNumber, episode.episodeNumber)
				},
				title = episode.name,
				voteAverage = episode.voteAverage,
				overview = episode.overview,
				posterPath = episode.stillPath
			)
		}
	}
}

@Composable
@Preview(showBackground = true)
fun RatingsScreenPreview() {
	RatingsScreen(
		navigateToMovieDetails = {},
		navigateToTvShowDetails = {},
		navigateToTvEpisodeDetails = { _, _, _ -> },
		uiState = mutableStateOf(
			RatingsUiState.Success(
				ratedMovies = persistentListOf(
					Movie(id = 1, title = "Sample Movie", voteAverage = 8.5f, overview = "Overview", posterPath = null)
				),
				ratedTvShows = persistentListOf(
					TvShow(
						id = 2,
						name = "Sample Show",
						voteAverage = 7.9f,
						overview = "Show Overview",
						posterPath = null
					)
				),
				ratedTvEpisodes = persistentListOf(
					TvEpisodeDetail(
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
					)
				)
			)
		)
	)

}
