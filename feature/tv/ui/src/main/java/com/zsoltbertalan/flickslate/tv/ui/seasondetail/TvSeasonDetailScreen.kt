package com.zsoltbertalan.flickslate.tv.ui.seasondetail

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.ui.compose.component.BASE_IMAGE_PATH
import com.zsoltbertalan.flickslate.shared.ui.compose.component.HEADER_IMAGE_ASPECT_RATIO
import com.zsoltbertalan.flickslate.shared.ui.compose.component.rating.RatingSection
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Dimens
import com.zsoltbertalan.flickslate.tv.ui.R

@Composable
fun TvSeasonDetailScreen(
	seriesId: Int,
	seasonNumber: Int,
	bgColor: Int,
	bgColorDim: Int,
	modifier: Modifier = Modifier,
	viewModel: TvSeasonDetailViewModel = hiltViewModel(),
) {
	LaunchedEffect(seriesId, seasonNumber, bgColor, bgColorDim) {
		viewModel.load(seriesId, seasonNumber, bgColor, bgColorDim)
	}

	val uiState by viewModel.uiState.collectAsState()
	val context = LocalContext.current

	LaunchedEffect(uiState.showRatingToast) {
		if (uiState.showRatingToast) {
			android.widget.Toast.makeText(context, "Thanks for rating!", android.widget.Toast.LENGTH_SHORT).show()
			viewModel.toastShown()
		}
	}

	Box(
		modifier = modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		if (uiState.isLoading) {
			CircularProgressIndicator()
		} else if (uiState.failure != null) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center,
				modifier = Modifier.padding(horizontal = Dimens.marginLarge)
			) {
				Text(text = uiState.failure?.message ?: stringResource(id = R.string.error_unknown))
				Spacer(modifier = Modifier.height(Dimens.marginLarge))
				Button(onClick = { viewModel.fetchSeasonDetails() }) {
					Text(text = stringResource(id = R.string.retry))
				}
			}
		} else {
			TvSeasonDetailContent(
				uiState = uiState,
				onEpisodeClick = { episodeId -> viewModel.toggleEpisodeExpanded(episodeId) },
				onRateEpisode = { episodeNumber, rating -> viewModel.rateEpisode(episodeNumber, rating) },
				onChangeEpisode = { episodeNumber, rating -> viewModel.changeEpisodeRating(episodeNumber, rating) },
				onDeleteEpisode = { episodeNumber -> viewModel.deleteEpisodeRating(episodeNumber) },
				context,
			)
		}
	}
}

@Composable
private fun TvSeasonDetailContent(
	uiState: TvSeasonDetailUiState,
	onEpisodeClick: (Int) -> Unit,
	onRateEpisode: (Int, Float) -> Unit,
	onChangeEpisode: (Int, Float) -> Unit,
	onDeleteEpisode: (Int) -> Unit,
	context: Context,
) {
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(
				brush = Brush.linearGradient(
					listOf(Color(uiState.bgColor), Color(uiState.bgColorDim))
				),
			)
			.testTag("Tv Season Detail Column"),
		contentPadding = PaddingValues(vertical = Dimens.marginLarge)
	) {
		item {
			uiState.seasonDetail?.name?.let {
				Text(
					text = it,
					style = MaterialTheme.typography.titleLarge,
					modifier = Modifier.padding(horizontal = Dimens.marginLarge)
				)
			}
			Spacer(modifier = Modifier.height(Dimens.marginLarge))
			uiState.seasonDetail?.posterPath?.let {
				Image(
					painter = rememberAsyncImagePainter(BASE_IMAGE_PATH + it),
					contentDescription = uiState.seasonDetail.name ?: "Season Poster",
					modifier = Modifier
						.fillMaxWidth()
						.aspectRatio(HEADER_IMAGE_ASPECT_RATIO),
					contentScale = ContentScale.Crop
				)
			}
			Column(modifier = Modifier.padding(all = Dimens.marginLarge)) {
				uiState.seasonDetail?.airDate?.let {
					Text(
						text = stringResource(R.string.air_date, it),
						style = MaterialTheme.typography.bodyMedium
					)
					Spacer(modifier = Modifier.height(Dimens.marginNormal))
				}

				val seasonOverview = (uiState.seasonDetail?.overview ?: "").ifEmpty {
					stringResource(id = R.string.no_overview_available)
				}
				Text(text = seasonOverview, style = MaterialTheme.typography.bodyLarge)
				Spacer(modifier = Modifier.height(Dimens.marginLarge))

				if (uiState.seasonDetail?.episodes?.isNotEmpty() == true) {
					Text(
						text = stringResource(R.string.episodes_title),
						style = MaterialTheme.typography.titleMedium
					)
					Spacer(modifier = Modifier.height(Dimens.marginNormal))

				}
			}

			HorizontalDivider(color = Colors.onBackground)
		}

		items(
			items = uiState.seasonDetail?.episodes ?: emptyList(),
			key = { item -> item.id }
		) { item ->
			EpisodeItem(
				episode = item,
				isExpanded = item.id == uiState.expandedEpisodeId,
				onEpisodeClick = { onEpisodeClick(item.id) },
				isLoggedIn = uiState.isLoggedIn,
				onRateEpisode = onRateEpisode,
				onChangeEpisode = onChangeEpisode,
				onDeleteEpisode = onDeleteEpisode,
				context = context,
			)
			HorizontalDivider(color = Colors.onBackground)
		}
	}
}

@Composable
private fun EpisodeItem(
	episode: TvEpisodeDetail,
	isExpanded: Boolean,
	onEpisodeClick: () -> Unit,
	isLoggedIn: Boolean,
	onRateEpisode: (Int, Float) -> Unit,
	onChangeEpisode: (Int, Float) -> Unit,
	onDeleteEpisode: (Int) -> Unit,
	context: Context
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.clickable(onClick = onEpisodeClick)
			.padding(horizontal = Dimens.marginLarge, vertical = Dimens.marginLarge)
	) {
		episode.stillPath?.let {
			Image(
				painter = rememberAsyncImagePainter(
					model = BASE_IMAGE_PATH + it,
					error = painterResource(id = com.zsoltbertalan.flickslate.shared.ui.R.drawable.ic_movie),
					fallback = painterResource(id = com.zsoltbertalan.flickslate.shared.ui.R.drawable.ic_movie)
				),
				contentDescription = "Still image for episode: ${episode.name}",
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
			maxLines = if (isExpanded) Int.MAX_VALUE else 3
		)

		if (isLoggedIn) {
			Spacer(modifier = Modifier.height(Dimens.marginNormal))
			val showRateLabel = episode.personalRating > -1f || isExpanded
			if (showRateLabel) {
				RatingSection(
					title = stringResource(id = R.string.rate_episode_title),
					titleTestTag = "Rate Episode ${episode.episodeNumber}",
					initialRating = episode.personalRating.takeIf { it > -1f } ?: 0f,
					isRated = episode.personalRating > -1f,
					isRatingInProgress = false,
					ratingLabelProvider = { context.getString(R.string.your_rating_value, it) },
					rateLabel = stringResource(id = R.string.rate),
					changeLabel = stringResource(id = R.string.change_rating),
					deleteLabel = stringResource(id = R.string.delete_rating),
					onRate = { rating -> onRateEpisode(episode.episodeNumber, rating) },
					onChange = { rating -> onChangeEpisode(episode.episodeNumber, rating) },
					onDelete = { onDeleteEpisode(episode.episodeNumber) },
					modifier = Modifier.padding(top = Dimens.marginNormal)
				)
			}
		}
	}
}
