package com.zsoltbertalan.flickslate.account.ui.ratings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Dimens

@Composable
fun RatingsScreen(viewModel: RatingsViewModel = hiltViewModel()) {

	val uiState by viewModel.uiState.collectAsState()

	when (val state = uiState) {
		is RatingsUiState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
			CircularProgressIndicator()
		}

		is RatingsUiState.Success -> RatingsContent(state.ratedMovies, state.ratedTvShows, state.ratedTvEpisodes)

		is RatingsUiState.Error -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
			Text(text = state.message)
		}
	}
}

@Composable
private fun RatingsContent(
	ratedMovies: List<Movie>,
	ratedTvShows: List<TvShow>,
	ratedTvEpisodes: List<TvEpisodeDetail>
) {
	LazyColumn(modifier = Modifier.padding(16.dp)) {
		item {
			Text("Rated Movies", style = MaterialTheme.typography.titleLarge)
			Spacer(modifier = Modifier.height(Dimens.marginNormal))
		}
		items(ratedMovies) { movie ->
			ListItem(
				headlineContent = { Text(movie.title) },
				modifier = Modifier.clickable { /* Handle movie click */ },
			)
			HorizontalDivider()
		}

		item {
			Spacer(modifier = Modifier.height(Dimens.marginLarge))
			Text("Rated TV Shows", style = MaterialTheme.typography.titleLarge)
			Spacer(modifier = Modifier.height(Dimens.marginNormal))
		}
		items(ratedTvShows) { tvShow ->
			ListItem(
				headlineContent = { Text(tvShow.name) },
				modifier = Modifier.clickable { /* Handle TV show click */ },
			)
			HorizontalDivider()
		}

		item {
			Spacer(modifier = Modifier.height(Dimens.marginLarge))
			Text("Rated TV Episodes", style = MaterialTheme.typography.titleLarge)
			Spacer(modifier = Modifier.height(Dimens.marginNormal))
		}
		items(ratedTvEpisodes) { episode ->
			ListItem(
				headlineContent = { Text(episode.name ?: "") },
				modifier = Modifier.clickable { /* Handle episode click */ },
			)
			HorizontalDivider()
		}
	}
}
