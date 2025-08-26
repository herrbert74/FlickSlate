package com.zsoltbertalan.flickslate.movies.ui.moviedetails

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
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

val Context.isDarkMode
	get() = resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES

@Composable
fun MovieDetailScreen(
	setTitle: (String) -> Unit,
	setBackgroundColor: (Color) -> Unit,
	modifier: Modifier = Modifier,
	viewModel: MovieDetailViewModel = hiltViewModel(),
) {
	val detail = viewModel.movieStateData.collectAsStateWithLifecycle().value

	val bg = Colors.surface
	val bgDim = Colors.surfaceDim
	val context = LocalContext.current
	var color1 by remember { mutableStateOf(bg) }
	var color2 by remember { mutableStateOf(bgDim) }

	val imageUrl = rememberSaveable(detail.movieDetail?.backdropPath) {
		mutableStateOf(detail.movieDetail?.backdropPath)
	}

	// To prevent LambdaParameterInRestartableEffect
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

	if (detail.movieDetail != null) {
		setTitle(detail.movieDetail.title.toString())
		LazyColumn(modifier.fillMaxSize()) {
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
