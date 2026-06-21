package com.zsoltbertalan.flickslate.movies.ui.moviedetails

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.HeartBroken
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetailWithImages
import com.zsoltbertalan.flickslate.movies.ui.R
import com.zsoltbertalan.flickslate.shared.ui.compose.component.BASE_IMAGE_PATH
import com.zsoltbertalan.flickslate.shared.ui.compose.component.GenreChips
import com.zsoltbertalan.flickslate.shared.ui.compose.component.HEADER_IMAGE_ASPECT_RATIO
import com.zsoltbertalan.flickslate.shared.ui.compose.component.TitleText
import com.zsoltbertalan.flickslate.shared.ui.compose.component.rating.RatingSection
import com.zsoltbertalan.flickslate.shared.ui.compose.component.rating.RatingToastMessage
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Dimens
import com.zsoltbertalan.flickslate.shared.ui.compose.util.convertImageUrlToBitmap
import com.zsoltbertalan.flickslate.shared.ui.compose.util.extractColorsFromBitmap
import com.zsoltbertalan.flickslate.shared.ui.navigation.LocalResultStore
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

val Context.isDarkMode
	get() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

@Composable
fun MovieDetailScreen(
	movieId: Int,
	setTitle: (String) -> Unit,
	setBackgroundColor: (Color) -> Unit,
	modifier: Modifier = Modifier,
	viewModel: MovieDetailViewModel = assistedMetroViewModel(),
) {
	val resultStore = LocalResultStore.current

	LaunchedEffect(movieId) {
		viewModel.load(movieId)
	}

	val detail = viewModel.movieStateData.collectAsStateWithLifecycle().value

	val bg = Colors.surface
	val bgDim = Colors.surfaceDim
	val context = LocalContext.current
	var color1 by remember { mutableStateOf(bg) }
	var color2 by remember { mutableStateOf(bgDim) }

	val imageUrl = rememberSaveable(detail.movieDetail?.backdropPath) {
		mutableStateOf(detail.movieDetail?.backdropPath)
	}

	val setLatestBackgroundColor by rememberUpdatedState(setBackgroundColor)

	ImageColorExtractor(
		imageUrl = imageUrl.value,
		context = context,
		bg = bg,
		bgDim = bgDim,
		onExtractColors = { vibrant, muted ->
			color1 = vibrant
			color2 = muted
			setLatestBackgroundColor(vibrant)
		},
	)

	RatingToastHandler(
		showRatingToast = detail.showRatingToast,
		ratingToastMessage = detail.ratingToastMessage,
		context = context,
		onShowToast = viewModel::toastShown,
		onResult = { key, value -> resultStore.setResult(key, value) },
	)

	FavoriteToastHandler(
		showFavoriteToast = detail.showFavoriteToast,
		onShowToast = viewModel::toastShown,
		onResult = { key, value -> resultStore.setResult(key, value) },
	)

	val movieDetail = detail.movieDetail ?: return

	setTitle(movieDetail.title.toString())

	val currentRating = movieDetail.personalRating.takeIf { it > -1f }
		?: detail.lastRatedValue ?: 0f
	val sliderPosition by remember(movieDetail.personalRating, detail.lastRatedValue) {
		mutableFloatStateOf(currentRating)
	}

	MovieDetailBody(
		movieDetail = movieDetail,
		detail = detail,
		sliderPosition = sliderPosition,
		color1 = color1,
		color2 = color2,
		onRate = viewModel::rateMovie,
		onChangeRating = viewModel::changeRating,
		onDeleteRating = viewModel::deleteRating,
		onToggleFavorite = viewModel::toggleFavorite,
		modifier = modifier,
	)
}

@Composable
private fun MovieDetailBody(
	movieDetail: MovieDetailWithImages,
	detail: MovieDetailState,
	sliderPosition: Float,
	color1: Color,
	color2: Color,
	onRate: (Float) -> Unit,
	onChangeRating: (Float) -> Unit,
	onDeleteRating: () -> Unit,
	onToggleFavorite: () -> Unit,
	modifier: Modifier = Modifier,
) {
	LazyColumn(
		modifier
			.fillMaxSize()
			.testTag("Movie Detail Column")
	) {
		item {
			Image(
				painter = rememberAsyncImagePainter(BASE_IMAGE_PATH + movieDetail.backdropPath),
				contentDescription = "",
				modifier = Modifier
					.fillMaxWidth()
					.aspectRatio(HEADER_IMAGE_ASPECT_RATIO),
				contentScale = ContentScale.Crop
			)

			Column(
				modifier = Modifier
					.background(
						brush = Brush.linearGradient(
							listOf(Color(color1.value), Color(color2.value))
						),
					)
					.padding(bottom = 50.dp)
			) {
				MovieDetailHeaderRow(movieDetail)
				MovieDetailGenresSection(movieDetail)
				MovieDetailOverviewSection(overview = movieDetail.overview)
				Spacer(modifier = Modifier.height(16.dp))

				if (detail.isLoggedIn) {
					MovieDetailLoggedInSection(
						detail = detail,
						sliderPosition = sliderPosition,
						onRate = onRate,
						onChangeRating = onChangeRating,
						onDeleteRating = onDeleteRating,
						onToggleFavorite = onToggleFavorite,
					)
				}

				Spacer(modifier = Modifier.height(16.dp))
				TitleText(
					modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
					title = "Image gallery"
				)
				if (movieDetail.movieImages.backdrops.isNotEmpty()) {
					val pagerState = rememberPagerState(pageCount = { movieDetail.movieImages.backdrops.size })
					HorizontalPager(state = pagerState) { page ->
						Image(
							painter = rememberAsyncImagePainter(
								BASE_IMAGE_PATH + movieDetail.movieImages.backdrops[page].filePath
							),
							contentDescription = "",
							modifier = Modifier
								.fillMaxWidth()
								.aspectRatio(HEADER_IMAGE_ASPECT_RATIO),
							contentScale = ContentScale.Crop
						)
					}
				}
				Spacer(modifier = Modifier.height(200.dp))
			}
		}
	}
}

@Composable
private fun MovieDetailHeaderRow(
	movieDetail: MovieDetailWithImages,
) {
	Row(modifier = Modifier.height(Dimens.listSingleItemHeight)) {
		Text(
			modifier = Modifier.padding(16.dp),
			text = movieDetail.title ?: ""
		)
		VerticalDivider(
			modifier = Modifier.padding(vertical = 16.dp),
			color = Colors.onSurface
		)
		Text(
			modifier = Modifier.padding(16.dp),
			text = movieDetail.voteAverage.toString()
		)
	}
}

@Composable
private fun MovieDetailGenresSection(
	movieDetail: MovieDetailWithImages,
) {
	Column(modifier = Modifier.padding(horizontal = 8.dp)) {
		TitleText(
			modifier = Modifier.padding(vertical = 16.dp),
			title = "Genres"
		)
		movieDetail.genres.takeIf { it.isNotEmpty() }?.let { GenreChips(it) }
	}
}

@Composable
private fun MovieDetailOverviewSection(overview: String?) {
	Column {
		TitleText(
			modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
			title = "Story Line"
		)
		overview?.let {
			Text(
				modifier = Modifier.padding(16.dp),
				text = it
			)
		}
	}
}

@Composable
private fun MovieDetailLoggedInSection(
	detail: MovieDetailState,
	sliderPosition: Float,
	onRate: (Float) -> Unit,
	onChangeRating: (Float) -> Unit,
	onDeleteRating: () -> Unit,
	onToggleFavorite: () -> Unit,
) {
	val context = LocalContext.current
	val favoriteLabel = if (detail.isFavorite) {
		stringResource(id = R.string.remove_from_favorites)
	} else {
		stringResource(id = R.string.add_to_favorites)
	}
	val favoriteIcon = if (detail.isFavorite) Icons.Outlined.HeartBroken else Icons.Filled.Favorite

	Column {
		RatingSection(
			title = stringResource(id = R.string.rate_movie_title),
			titleTestTag = "Rate this movie title",
			initialRating = sliderPosition,
			isRated = detail.isRated,
			isRatingInProgress = detail.isRatingInProgress,
			ratingLabelProvider = { context.getString(R.string.your_rating_value, it) },
			rateLabel = stringResource(id = R.string.rate),
			changeLabel = stringResource(id = R.string.update_rating),
			deleteLabel = stringResource(id = R.string.delete_rating),
			onRate = onRate,
			onChange = onChangeRating,
			onDelete = onDeleteRating,
		)

		Spacer(modifier = Modifier.height(16.dp))

		Button(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp)
				.testTag("Favorite Button"),
			enabled = !detail.isFavoriteInProgress,
			onClick = onToggleFavorite,
		) {
			Icon(imageVector = favoriteIcon, contentDescription = null)
			Spacer(modifier = Modifier.width(8.dp))
			Text(text = favoriteLabel)
		}
	}
}

@Composable
private fun ImageColorExtractor(
	imageUrl: String?,
	context: Context,
	bg: Color,
	bgDim: Color,
	onExtractColors: (Color, Color) -> Unit,
) {
	val currentOnExtractColors by rememberUpdatedState(onExtractColors)
	LaunchedEffect(imageUrl) {
		imageUrl ?: return@LaunchedEffect
		val bitmap = convertImageUrlToBitmap(
			imageUrl = BASE_IMAGE_PATH + imageUrl,
			context = context,
		) ?: return@LaunchedEffect
		val palette = extractColorsFromBitmap(bitmap = bitmap, isDarkMode = context.isDarkMode)
		val vibrant = Color((palette["vibrant"] ?: bg.toString()).toColorInt())
		val muted = Color((palette["muted"] ?: bgDim.toString()).toColorInt())
		currentOnExtractColors(vibrant, muted)
	}
}

@Composable
private fun RatingToastHandler(
	showRatingToast: Boolean,
	ratingToastMessage: RatingToastMessage?,
	context: Context,
	onShowToast: () -> Unit,
	onResult: (String, Boolean) -> Unit,
) {
	val currentOnShowToast by rememberUpdatedState(onShowToast)
	val currentOnResult by rememberUpdatedState(onResult)
	LaunchedEffect(showRatingToast, ratingToastMessage) {
		if (showRatingToast && ratingToastMessage != null) {
			val message = when (ratingToastMessage) {
				RatingToastMessage.Success -> R.string.rating_thanks
				RatingToastMessage.Updated -> R.string.rating_updated
				RatingToastMessage.Deleted -> R.string.rating_removed
			}
			Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()
			currentOnShowToast()
			currentOnResult("RatingChanged", true)
		}
	}
}

@Composable
private fun FavoriteToastHandler(
	showFavoriteToast: Boolean,
	onShowToast: () -> Unit,
	onResult: (String, Boolean) -> Unit,
) {
	val currentOnShowToast by rememberUpdatedState(onShowToast)
	val currentOnResult by rememberUpdatedState(onResult)
	LaunchedEffect(showFavoriteToast) {
		if (showFavoriteToast) {
			currentOnShowToast()
			currentOnResult("FavoriteChanged", true)
		}
	}
}
