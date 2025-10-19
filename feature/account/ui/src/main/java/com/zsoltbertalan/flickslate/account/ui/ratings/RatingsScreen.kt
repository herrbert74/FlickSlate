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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.ui.compose.component.ShowCard
import kotlinx.collections.immutable.ImmutableList

@Composable
fun RatingsScreen(
	modifier: Modifier = Modifier,
	viewModel: RatingsViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsState()

	when (val state = uiState) {
		is RatingsUiState.Loading -> Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
			CircularProgressIndicator()
		}

		is RatingsUiState.Success ->
			RatingsContent(state.ratedMovies, state.ratedTvShows, state.ratedTvEpisodes, modifier)

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
	modifier: Modifier = Modifier
) {
	LazyColumn(
		modifier = modifier.fillMaxSize(),
		contentPadding = PaddingValues(vertical = 16.dp)
	) {
		items(ratedMovies) { movie ->
			ShowCard(
				modifier = Modifier.clickable { /* Handle movie click */ },
				title = movie.title,
				voteAverage = movie.voteAverage,
				overview = movie.overview,
				posterPath = movie.posterPath
			)
		}
		items(ratedTvShows) { tvShow ->
			ShowCard(
				modifier = Modifier.clickable { /* Handle movie click */ },
				title = tvShow.name,
				voteAverage = tvShow.voteAverage,
				overview = tvShow.overview,
				posterPath = tvShow.posterPath
			)
		}
		items(ratedTvEpisodes) { episode ->
			ShowCard(
				modifier = Modifier.clickable { /* Handle movie click */ },
				title = episode.name,
				voteAverage = episode.voteAverage,
				overview = episode.overview,
				posterPath = episode.stillPath
			)
		}
	}
}
