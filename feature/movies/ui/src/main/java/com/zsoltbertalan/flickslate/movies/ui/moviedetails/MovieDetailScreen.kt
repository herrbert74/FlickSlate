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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.zsoltbertalan.flickslate.movies.ui.R
import com.zsoltbertalan.flickslate.shared.ui.compose.component.BASE_IMAGE_PATH
import com.zsoltbertalan.flickslate.shared.ui.compose.component.GenreChips
import com.zsoltbertalan.flickslate.shared.ui.compose.component.HEADER_IMAGE_ASPECT_RATIO
import com.zsoltbertalan.flickslate.shared.ui.compose.component.TitleText
import com.zsoltbertalan.flickslate.shared.ui.compose.component.rating.RatingSection
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Dimens
import com.zsoltbertalan.flickslate.shared.ui.compose.util.convertImageUrlToBitmap
import com.zsoltbertalan.flickslate.shared.ui.compose.util.extractColorsFromBitmap

val Context.isDarkMode
	get() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

@Composable
fun MovieDetailScreen(
	movieId: Int,
	setTitle: (String) -> Unit,
	setBackgroundColor: (Color) -> Unit,
	modifier: Modifier = Modifier,
	viewModel: MovieDetailViewModel = hiltViewModel(),
) {
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

	LaunchedEffect(imageUrl) {
		imageUrl.value?.let {
			val bitmap = convertImageUrlToBitmap(imageUrl = BASE_IMAGE_PATH + it, context = context)
			if (bitmap != null) {
				val vibrantColor = extractColorsFromBitmap(
					bitmap = bitmap,
					isDarkMode = context.isDarkMode
				)["vibrant"] ?: bg.toString()
				color1 = Color(vibrantColor.toColorInt())
				setLatestBackgroundColor(color1)
				val darkVibrantColor = extractColorsFromBitmap(
					bitmap = bitmap,
					isDarkMode = context.isDarkMode
				)["muted"] ?: bgDim.toString()
				color2 = Color(darkVibrantColor.toColorInt())
			}
		}
	}

	LaunchedEffect(detail.showRatingToast, detail.ratingToastMessage) {
		if (detail.showRatingToast && detail.ratingToastMessage != null) {
			val message = when (detail.ratingToastMessage) {
				RatingToastMessage.Success -> R.string.rating_thanks
				RatingToastMessage.Updated -> R.string.rating_updated
				RatingToastMessage.Deleted -> R.string.rating_removed
			}
			Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()
			viewModel.toastShown()
		}
	}

	val currentRating = detail.movieDetail?.personalRating?.takeIf { it > -1f }
		?: detail.lastRatedValue ?: 0f
	val sliderPosition by remember(detail.movieDetail?.personalRating, detail.lastRatedValue) {
		mutableFloatStateOf(currentRating)
	}

	if (detail.movieDetail != null) {
		setTitle(detail.movieDetail.title.toString())
		LazyColumn(
			modifier
				.fillMaxSize()
				.testTag("Movie Detail Column")
		) {
			item {
				Image(
					painter = rememberAsyncImagePainter(BASE_IMAGE_PATH + detail.movieDetail.backdropPath),
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
					Row(modifier = Modifier.height(Dimens.listSingleItemHeight)) {
						Text(
							modifier = Modifier.padding(16.dp),
							text = detail.movieDetail.title ?: ""
						)
						VerticalDivider(
							modifier = Modifier.padding(vertical = 16.dp),
							color = Colors.onSurface
						)
						Text(
							modifier = Modifier.padding(16.dp),
							text = detail.movieDetail.voteAverage.toString()
						)
					}
					Column(
						modifier = Modifier
							.padding(horizontal = 8.dp)
					) {
						TitleText(
							modifier = Modifier.padding(vertical = 16.dp),
							title = "Genres"
						)
						detail.movieDetail.genres.takeIf { it.isNotEmpty() }?.let {
							GenreChips(it)
						}
					}
					TitleText(
						modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
						title = "Story Line"
					)
					detail.movieDetail.overview?.let {
						Text(
							modifier = Modifier.padding(16.dp),
							text = it
						)
					}
					Spacer(modifier = Modifier.height(16.dp))

					if (detail.isLoggedIn) {
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
							onRate = viewModel::rateMovie,
							onChange = viewModel::changeRating,
							onDelete = viewModel::deleteRating,
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
						title = "Image gallery"
					)
					if (detail.movieDetail.movieImages.backdrops.isNotEmpty()) {
						val pagerState = rememberPagerState(pageCount = {
							detail.movieDetail.movieImages.backdrops.size
						})
						HorizontalPager(state = pagerState) { page ->
							Image(
								painter = rememberAsyncImagePainter(
									BASE_IMAGE_PATH + detail.movieDetail.movieImages.backdrops[page].filePath
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
