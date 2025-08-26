package com.zsoltbertalan.flickslate.tv.ui.seasondetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.zsoltbertalan.flickslate.shared.ui.compose.component.BASE_IMAGE_PATH
import com.zsoltbertalan.flickslate.shared.ui.compose.component.HEADER_IMAGE_ASPECT_RATIO
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Dimens
import com.zsoltbertalan.flickslate.tv.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.tv.ui.R

@Composable
fun TvSeasonDetailScreen(
	modifier: Modifier = Modifier,
	viewModel: TvSeasonDetailViewModel = hiltViewModel(),
) {
	val uiState by viewModel.uiState.collectAsState()

	Box(
		modifier = modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		if (uiState.isLoading) {
			CircularProgressIndicator()
		} else if (uiState.failure != null) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center
			) {
				Text(text = uiState.failure?.message ?: stringResource(id = R.string.error_unknown))
				Spacer(modifier = Modifier.height(Dimens.marginLarge))
				Button(onClick = { viewModel.fetchSeasonDetails() }) {
					Text(text = stringResource(id = R.string.retry))
				}
			}
		} else {
			TvSeasonDetailContent(uiState)
		}
	}
}

@Composable
private fun TvSeasonDetailContent(uiState: TvSeasonDetailUiState) {
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(
				brush = Brush.linearGradient(
					listOf(Color(uiState.bgColor), Color(uiState.bgColorDim))
				),
			),
		contentPadding = PaddingValues(Dimens.marginLarge)
	) {
		item {
			Column {
				uiState.seasonDetail?.name?.let {
					Text(
						text = it,
						style = MaterialTheme.typography.titleLarge
					)
				}
				Spacer(modifier = Modifier.height(Dimens.marginLarge))
				uiState.seasonDetail?.posterPath?.let {
					Image(
						painter = rememberAsyncImagePainter(BASE_IMAGE_PATH + it),
						contentDescription = "",
						modifier = Modifier
							.fillMaxWidth()
							.aspectRatio(HEADER_IMAGE_ASPECT_RATIO),
						contentScale = ContentScale.Crop
					)
				}

				uiState.seasonDetail?.airDate?.let {
					Spacer(modifier = Modifier.height(Dimens.marginNormal))
					Text(
						text = stringResource(R.string.air_date, it),
						style = MaterialTheme.typography.bodyMedium
					)
					Spacer(modifier = Modifier.height(Dimens.marginNormal))
				}

				val seasonOverview =
					uiState.seasonDetail?.overview ?: stringResource(id = R.string.no_overview_available)
				Text(text = seasonOverview, style = MaterialTheme.typography.bodyLarge)
				Spacer(modifier = Modifier.height(Dimens.marginLarge))

				if (uiState.seasonDetail?.episodes?.isNotEmpty() == true) {
					Text(
						text = stringResource(R.string.episodes_title),
						style = MaterialTheme.typography.titleMedium
					)
					Spacer(modifier = Modifier.height(Dimens.marginNormal))
					HorizontalDivider(color = Colors.onBackground)
				}
			}
		}

		itemsIndexed(
			items = uiState.seasonDetail?.episodes ?: emptyList(),
			key = { _, item -> item.id }
		) { index, item ->
			EpisodeItem(episode = item)
			HorizontalDivider(color = Colors.onBackground)
		}
	}
}

@Composable
private fun EpisodeItem(episode: TvEpisodeDetail) {
	Column(modifier = Modifier.padding(vertical = Dimens.marginNormal)) {
		episode.stillPath?.let {
			AsyncImage(
				model = ImageRequest.Builder(LocalContext.current)
					.data(
						"https://image.tmdb.org/t/p/w500$it"
					)
					.crossfade(true)
					.build(),
				// placeholder = painterResource(drawable.generic_placeholder),
				// error = painterResource(drawable.generic_placeholder),
				contentDescription = "Episode Still: ${episode.name}",
				modifier = Modifier
					.fillMaxWidth()
					.aspectRatio(HEADER_IMAGE_ASPECT_RATIO),
				contentScale = ContentScale.Crop
			)
			Spacer(modifier = Modifier.height(Dimens.marginNormal))
		}
		Text(
			text = "${episode.episodeNumber}. ${episode.name ?: stringResource(id = R.string.untitled_episode)}",
			style = MaterialTheme.typography.titleSmall
		)
		Spacer(modifier = Modifier.height(4.dp))
		Text(
			text = episode.overview,
			style = MaterialTheme.typography.bodyMedium,
			maxLines = 3
		)
	}
}
