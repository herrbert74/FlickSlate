package com.zsoltbertalan.flickslate.ui.moviedetails

import android.graphics.Color.parseColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.zsoltbertalan.flickslate.BASE_IMAGE_PATH
import com.zsoltbertalan.flickslate.design.Colors
import com.zsoltbertalan.flickslate.design.LocalAppColors
import com.zsoltbertalan.flickslate.ui.component.GenreChips
import com.zsoltbertalan.flickslate.util.convertImageUrlToBitmap
import com.zsoltbertalan.flickslate.util.extractColorsFromBitmap

@Composable
fun MovieDetailScreen(
	modifier: Modifier = Modifier, viewModel: MovieDetailViewModel = hiltViewModel()
) {
	val detail = viewModel.movieStateData.collectAsStateWithLifecycle().value

	val bg = Colors.background
	val bgDim = Colors.surfaceDim
	val context = LocalContext.current
	var color1 by remember { mutableStateOf(bg) }
	var color2 by remember { mutableStateOf(bgDim) }

	val imageUrl = rememberSaveable(detail.backdropPath) {
		mutableStateOf(detail.backdropPath)
	}

	LaunchedEffect(imageUrl) {
		imageUrl.value?.let {
			val bitmap = convertImageUrlToBitmap(
				imageUrl = BASE_IMAGE_PATH + it, context = context
			)
			if (bitmap != null) {
				val vibrantColor = extractColorsFromBitmap(
					bitmap = bitmap
				)["vibrant"] ?: bg.toString()
				color1 = Color(parseColor(vibrantColor))
				val darkVibrantColor = extractColorsFromBitmap(
					bitmap = bitmap
				)["dark"] ?: bgDim.toString()
				color2 = Color(parseColor(darkVibrantColor))
			}
		}
	}

	LazyColumn {
		item {
			Image(
				painter = rememberAsyncImagePainter(BASE_IMAGE_PATH + detail.backdropPath),
				contentDescription = "",
				modifier = Modifier
					.fillMaxWidth()
					.aspectRatio(16f / 9f),
				contentScale = ContentScale.Crop
			)
			Column(
				modifier = Modifier
					.background(
						brush = Brush.linearGradient(
							0.0f to Color(color1.value), 0.8f to Color(color2.value)
						),
					)
					.padding(bottom = 50.dp)
			) {
				Row(modifier = Modifier.height(50.dp)) {
					Text(
						modifier = modifier.padding(16.dp), text = detail.title ?: ""
					)
					VerticalDivider(
						modifier = Modifier.padding(vertical = 16.dp)
					)
					Text(
						modifier = modifier.padding(16.dp), text = detail.voteAverage.toString()
					)
				}
				Column(
					modifier = Modifier
						.padding(horizontal = 16.dp)
				) {
					Text(
						text = "Genres"
					)
					detail.genres.takeIf { it.isNotEmpty() }?.let {
						GenreChips(it)
					}
				}
				Text(
					modifier = modifier.padding(16.dp), text = "Story Line"
				)
				detail.overview?.let {
					Text(
						modifier = modifier.padding(16.dp), text = it
					)
				}
				Spacer(modifier = Modifier.height(200.dp))
			}
		}
	}
}
