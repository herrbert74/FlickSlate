package com.zsoltbertalan.flickslate.shared.ui.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.zsoltbertalan.flickslate.shared.ui.R
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors

@Composable
fun PosterImage(
	posterThumbnail: String?,
	imageWidth: Int,
	imageHeight: Int,
	modifier: Modifier = Modifier,
) {
	val painter = rememberAsyncImagePainter(
		model = ImageRequest.Builder(LocalContext.current)
			.data(posterThumbnail?.let { "$BASE_IMAGE_PATH$it" })
			.diskCachePolicy(CachePolicy.ENABLED)
			.size(imageWidth, imageHeight)
			.crossfade(true)
			.build()
	)

	Box(modifier = modifier.fillMaxSize()) {
		when (painter.state) {
			is AsyncImagePainter.State.Success -> Image(
				painter = painter,
				contentDescription = "Poster image",
				modifier = Modifier.fillMaxSize(),
				contentScale = ContentScale.Crop
			)
			is AsyncImagePainter.State.Error -> Icon(
				modifier = Modifier.align(Alignment.Center),
				painter = painterResource(id = R.drawable.ic_movie),
				contentDescription = "Error loading image",
			)
			is AsyncImagePainter.State.Loading -> CircularProgressIndicator(
				modifier = Modifier.align(Alignment.Center),
				color = Colors.primary
			)
			else -> // Empty state
				Icon(
					modifier = Modifier.align(Alignment.Center),
					painter = painterResource(id = R.drawable.ic_movie),
					contentDescription = "No image available",
				)
		}
	}
}
