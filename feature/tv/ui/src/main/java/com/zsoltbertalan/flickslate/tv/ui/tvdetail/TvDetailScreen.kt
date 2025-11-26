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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
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
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Dimens
import com.zsoltbertalan.flickslate.shared.ui.compose.util.convertImageUrlToBitmap
import com.zsoltbertalan.flickslate.shared.ui.compose.util.extractColorsFromBitmap
import com.zsoltbertalan.flickslate.tv.ui.R

val Context.isDarkMode
	get() = resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES

@Composable
fun TvDetailScreen(
	setTitle: (String) -> Unit,
	setBackgroundColor: (Color) -> Unit,
	navigateToSeasonDetails: (Int, Int, Color, Color, Int?) -> Unit,
	modifier: Modifier = Modifier,
	viewModel: TvDetailViewModel = hiltViewModel(),
) {
	val detail = viewModel.tvStateData.collectAsStateWithLifecycle().value

	val redirectedToSeasonDetail = rememberSaveable { mutableStateOf(false) }

	val bg = Colors.surface
	val bgDim = Colors.surfaceDim
	val context = LocalContext.current
	var color1 by remember { mutableStateOf(bg) }
	var color2 by remember { mutableStateOf(bgDim) }

	val imageUrl = rememberSaveable(detail.tvDetail?.backdropPath) {
		mutableStateOf(detail.tvDetail?.backdropPath)
	}

	// To prevent LambdaParameterInRestartableEffect
	val setLatestBackgroundColor by rememberUpdatedState(setBackgroundColor)
	val setLatestNavigateToSeasonDetails by rememberUpdatedState(navigateToSeasonDetails)

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
			val seasonToNavigateTo = viewModel.tvStateData.value.seasonNumber
			val episodeToNavigateTo = viewModel.tvStateData.value.episodeNumber
			if (seasonToNavigateTo != null && episodeToNavigateTo != null && !redirectedToSeasonDetail.value) {
				redirectedToSeasonDetail.value = true
				setLatestNavigateToSeasonDetails(
					detail.tvDetail!!.id!!,
					seasonToNavigateTo,
					color1,
					color2,
					episodeToNavigateTo
				)
			}

		}
	}

	LaunchedEffect(detail.showRatingToast) {
		if (detail.showRatingToast) {
			Toast.makeText(context, context.getString(R.string.rating_thanks), Toast.LENGTH_SHORT).show()
			viewModel.toastShown()
		}
	}

	if (detail.tvDetail != null) {
		setTitle(detail.tvDetail.title.toString())
		LazyColumn(modifier.fillMaxSize()) {
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
						TitleText(
							modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
							title = stringResource(id = R.string.rate_show_title)
						)
						if (detail.isRated) {
							Text(
								modifier = Modifier.padding(16.dp),
								text = stringResource(
									id = R.string.your_rating_value,
									(detail.tvDetail.personalRating.takeIf { it > -1f } ?: detail.lastRatedValue ?: 0f)
								)
							)
						} else {
							var sliderPosition by remember { mutableFloatStateOf(0f) }
							Column(modifier = Modifier.padding(horizontal = 16.dp)) {
								Slider(
									value = sliderPosition,
									onValueChange = { sliderPosition = it },
									valueRange = 0f..10f,
									steps = 9,
								)
								Text(text = stringResource(id = R.string.your_rating_value, sliderPosition))
								Button(onClick = { viewModel.rateTvShow(sliderPosition) }) {
									Text(stringResource(id = R.string.rate))
								}
							}
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
