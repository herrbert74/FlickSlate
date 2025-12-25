package com.zsoltbertalan.flickslate.tv.ui.tvdetail

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
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
import com.zsoltbertalan.flickslate.tv.ui.R

val Context.isDarkMode
	get() = resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES

@Composable
fun TvDetailScreen(
	seriesId: Int,
	setTitle: (String) -> Unit,
	setBackgroundColor: (Color) -> Unit,
	navigateToSeasonDetails: (Int, Int, Color, Color, Int?) -> Unit,
	modifier: Modifier = Modifier,
	seasonNumber: Int? = null,
	episodeNumber: Int? = null,
	viewModel: TvDetailViewModel = hiltViewModel(),
) {
	val resultStore = LocalResultStore.current

	LaunchedEffect(seriesId, seasonNumber, episodeNumber) {
		viewModel.load(seriesId, seasonNumber, episodeNumber)
	}

	val detail = viewModel.tvStateData.collectAsStateWithLifecycle().value

	val redirectedToSeasonDetail = rememberSaveable { mutableStateOf(false) }

	val bg = Colors.surface
	val bgDim = Colors.surfaceDim
	val context = LocalContext.current
	var color1 by remember { mutableStateOf(bg) }
	var color2 by remember { mutableStateOf(bgDim) }
	val currentRating = detail.tvDetail?.personalRating?.takeIf { it > -1f } ?: detail.lastRatedValue ?: 0f
	val sliderPosition by remember(detail.tvDetail?.personalRating, detail.lastRatedValue) {
		mutableFloatStateOf(currentRating)
	}

	val imageUrl = rememberSaveable(detail.tvDetail?.backdropPath) {
		mutableStateOf(detail.tvDetail?.backdropPath)
	}

	// To prevent LambdaParameterInRestartableEffect
	val setLatestBackgroundColor by rememberUpdatedState(setBackgroundColor)
	val setLatestNavigateToSeasonDetails by rememberUpdatedState(navigateToSeasonDetails)

	ImageColorExtractor(
		imageUrl = imageUrl.value,
		context = context,
		bg = bg,
		bgDim = bgDim,
		onExtractColors = { c1, c2 ->
			color1 = c1
			color2 = c2
			setLatestBackgroundColor(c1)
			if (seasonNumber != null && !redirectedToSeasonDetail.value) {
				redirectedToSeasonDetail.value = true
				setLatestNavigateToSeasonDetails(seriesId, seasonNumber, c1, c2, episodeNumber)
			}
		}
	)

	RatingToastHandler(
		showRatingToast = detail.showRatingToast,
		ratingToastMessage = detail.ratingToastMessage,
		context = context,
		onShowToast = viewModel::toastShown,
		onResult = { key, value -> resultStore.setResult(key, value) }
	)

	FavoriteToastHandler(
		showFavoriteToast = detail.showFavoriteToast,
		onShowToast = viewModel::toastShown,
		onResult = { key, value -> resultStore.setResult(key, value) }
	)

	if (detail.tvDetail != null) {
		setTitle(detail.tvDetail.title ?: "")
		LazyColumn(
			modifier
				.fillMaxSize()
				.testTag("Tv Detail Column")
		) {
			item {
				Image(
					painter = rememberAsyncImagePainter(BASE_IMAGE_PATH + detail.tvDetail.backdropPath),
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
					SubtitleRow(detail.tvDetail)

					Column(modifier = Modifier.padding(horizontal = Dimens.marginLarge)) {
						TitleText(
							modifier = Modifier.padding(vertical = 16.dp),
							title = "Genres"
						)
						detail.tvDetail.genres.takeIf { it.isNotEmpty() }?.let {
							GenreChips(it)
						}
					}

					TitleText(
						modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
						title = "Story Line"
					)
					detail.tvDetail.overview?.let {
						Text(
							modifier = Modifier.padding(16.dp),
							text = it
						)
					}
					Spacer(modifier = Modifier.height(16.dp))

					// Rating section
					if (detail.isLoggedIn) {
						RatingSection(
							title = stringResource(id = R.string.rate_show_title),
							titleTestTag = "Rate this show title",
							initialRating = sliderPosition,
							isRated = detail.isRated,
							isRatingInProgress = detail.isRatingInProgress,
							ratingLabelProvider = { context.getString(R.string.your_rating_value, it) },
							rateLabel = stringResource(id = R.string.rate),
							changeLabel = stringResource(id = R.string.change_rating),
							deleteLabel = stringResource(id = R.string.delete_rating),
							onRate = viewModel::rateTvShow,
							onChange = viewModel::changeTvRating,
							onDelete = viewModel::deleteTvRating,
						)

						Spacer(modifier = Modifier.height(16.dp))

						val favoriteLabel = if (detail.isFavorite) {
							stringResource(id = R.string.remove_from_favorites)
						} else {
							stringResource(id = R.string.add_to_favorites)
						}

						val favoriteIcon = if (detail.isFavorite) {
							Icons.Outlined.HeartBroken
						} else {
							Icons.Filled.Favorite
						}

						Button(
							modifier = Modifier
								.fillMaxWidth()
								.padding(horizontal = 16.dp)
								.testTag("Favorite Button"),
							enabled = !detail.isFavoriteInProgress,
							onClick = viewModel::toggleFavorite,
						) {
							Icon(imageVector = favoriteIcon, contentDescription = null)
							Spacer(modifier = Modifier.width(8.dp))
							Text(text = favoriteLabel)
						}
					}

					Spacer(modifier = Modifier.height(16.dp))

					TitleText(
						modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
						title = "Seasons"
					)
					detail.tvDetail.seasons.let {
						SeasonsRow(
							seasons = it,
							modifier = Modifier.padding(16.dp),
							onClick = { index ->
								navigateToSeasonDetails(
									detail.tvDetail.id!!,
									detail.tvDetail.seasons[index].seasonNumber,
									color1,
									color2,
									null
								)
							}
						)
					}
					Spacer(modifier = Modifier.height(16.dp))

					TitleText(
						modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
						title = "Image gallery"
					)
					if (detail.tvDetail.tvImages.backdrops.isNotEmpty()) {
						val pagerState = rememberPagerState(pageCount = {
							detail.tvDetail.tvImages.backdrops.size
						})
						HorizontalPager(state = pagerState) { page ->
							Image(
								painter = rememberAsyncImagePainter(
									BASE_IMAGE_PATH + detail.tvDetail.tvImages.backdrops[page].filePath
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
}

@Composable
private fun ImageColorExtractor(
	imageUrl: String?,
	context: Context,
	bg: Color,
	bgDim: Color,
	onExtractColors: (Color, Color) -> Unit
) {
	val currentOnExtractColors by rememberUpdatedState(onExtractColors)
	LaunchedEffect(imageUrl) {
		imageUrl?.let {
			val bitmap = convertImageUrlToBitmap(imageUrl = BASE_IMAGE_PATH + it, context = context)
			if (bitmap != null) {
				val vibrantColor = extractColorsFromBitmap(
					bitmap = bitmap,
					isDarkMode = context.isDarkMode
				)["vibrant"] ?: bg.toString()
				val color1 = Color(vibrantColor.toColorInt())

				val darkVibrantColor = extractColorsFromBitmap(
					bitmap = bitmap,
					isDarkMode = context.isDarkMode
				)["muted"] ?: bgDim.toString()
				val color2 = Color(darkVibrantColor.toColorInt())

				currentOnExtractColors(color1, color2)
			}
		}
	}
}

@Composable
private fun RatingToastHandler(
	showRatingToast: Boolean,
	ratingToastMessage: RatingToastMessage?,
	context: Context,
	onShowToast: () -> Unit,
	onResult: (String, Boolean) -> Unit
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
	onResult: (String, Boolean) -> Unit
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
