package com.zsoltbertalan.flickslate.tv.ui.tvdetail

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
import com.zsoltbertalan.flickslate.shared.compose.component.BASE_IMAGE_PATH
import com.zsoltbertalan.flickslate.shared.compose.component.GenreChips
import com.zsoltbertalan.flickslate.shared.compose.component.TitleText
import com.zsoltbertalan.flickslate.shared.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.compose.design.Dimens
import com.zsoltbertalan.flickslate.shared.compose.util.convertImageUrlToBitmap
import com.zsoltbertalan.flickslate.shared.compose.util.extractColorsFromBitmap

val Context.isDarkMode
	get() = resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES

private const val HEADER_IMAGE_ASPECT_RATIO = 16f / 9f

@Composable
fun TvDetailScreen(
	setTitle: (String) -> Unit,
	setBackgroundColor: (Color) -> Unit,
	modifier: Modifier = Modifier,
	viewModel: TvDetailViewModel = hiltViewModel(),
) {
	val detail = viewModel.tvStateData.collectAsStateWithLifecycle().value

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
					Row(Modifier.height(Dimens.listSingleItemHeight)) {
						Text(
							modifier = Modifier.padding(16.dp),
							text = detail.tvDetail.title ?: ""
						)
						VerticalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Colors.onSurface)
						Text(
							modifier = Modifier.padding(16.dp),
							text = detail.tvDetail.voteAverage.toString()
						)
					}

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

					TitleText(
						modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
						title = "Seasons"
					)
					detail.tvDetail.seasons.let {
						SeasonsRow(
							seasons = it,
							modifier = Modifier.padding(16.dp),
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
