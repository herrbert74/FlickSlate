package com.zsoltbertalan.flickslate.movies.ui.moviedetails

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Color.parseColor
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.zsoltbertalan.flickslate.shared.compose.component.BASE_IMAGE_PATH
import com.zsoltbertalan.flickslate.shared.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.compose.design.Dimens
import com.zsoltbertalan.flickslate.shared.compose.component.GenreChips
import com.zsoltbertalan.flickslate.shared.compose.util.convertImageUrlToBitmap
import com.zsoltbertalan.flickslate.shared.compose.util.extractColorsFromBitmap

val Context.isDarkMode
	get() = resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES

private const val HEADER_IMAGE_ASPECT_RATIO = 16f / 9f

@Composable
fun MovieDetailScreen(
	modifier: Modifier = Modifier, viewModel: MovieDetailViewModel = hiltViewModel(), popBackStack: () -> Boolean
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

	LaunchedEffect(imageUrl) {
		imageUrl.value?.let {
			val bitmap = convertImageUrlToBitmap(imageUrl = BASE_IMAGE_PATH + it, context = context)
			if (bitmap != null) {
				val vibrantColor = extractColorsFromBitmap(
					bitmap = bitmap,
					isDarkMode = context.isDarkMode
				)["vibrant"] ?: bg.toString()
				color1 = Color(parseColor(vibrantColor))
				val darkVibrantColor = extractColorsFromBitmap(
					bitmap = bitmap,
					isDarkMode = context.isDarkMode
				)["muted"] ?: bgDim.toString()
				color2 = Color(parseColor(darkVibrantColor))
			}
		}
	}

	Scaffold(
		modifier = modifier.fillMaxSize(),
		topBar = {
			TopAppBar(title = { Text("Movie Details") },
				navigationIcon = {
					IconButton(onClick = { popBackStack() }) {
						Icon(
							imageVector = Icons.AutoMirrored.Filled.ArrowBack,
							contentDescription = "Finish",
							tint = Colors.onSurface
						)
					}
				})
		}
	) { paddingValues ->
		if(detail.movieDetail != null)
		LazyColumn(Modifier.padding(paddingValues)) {
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
								listOf( Color(color1.value), Color(color2.value))
							),
						)
						.padding(bottom = 50.dp)
				) {
					Row(modifier = Modifier.height(Dimens.listSingleItemHeight)) {
						Text(
							modifier = Modifier.padding(16.dp), text = detail.movieDetail.title ?: ""
						)
						VerticalDivider(
							modifier = Modifier.padding(vertical = 16.dp), color = Colors.onSurface
						)
						Text(
							modifier = Modifier.padding(16.dp), text = detail.movieDetail.voteAverage.toString()
						)
					}
					Column(
						modifier = Modifier
							.padding(horizontal = 16.dp)
					) {
						Text(
							text = "Genres"
						)
						detail.movieDetail.genres.takeIf { it.isNotEmpty() }?.let {
							GenreChips(it)
						}
					}
					Text(
						modifier = Modifier.padding(16.dp), text = "Story Line"
					)
					detail.movieDetail.overview?.let {
						Text(
							modifier = Modifier.padding(16.dp), text = it
						)
					}
					Spacer(modifier = Modifier.height(200.dp))
				}
			}
		}
	}
}
