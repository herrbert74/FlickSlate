package com.zsoltbertalan.flickslate.shared.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.zsoltbertalan.flickslate.shared.R
import com.zsoltbertalan.flickslate.shared.compose.design.Colors

const val BASE_IMAGE_PATH = "https://image.tmdb.org/t/p/original"

private const val IMAGE_REQUEST_WIDTH = 200
private const val IMAGE_REQUEST_HEIGHT = 380

@Composable
fun ThumbnailCard(posterThumbnail: String, modifier: Modifier = Modifier) {
	val painter = rememberAsyncImagePainter(
		model = ImageRequest.Builder(LocalContext.current)
			.data("$BASE_IMAGE_PATH$posterThumbnail")
			.diskCachePolicy(CachePolicy.ENABLED)
			.size(IMAGE_REQUEST_WIDTH, IMAGE_REQUEST_HEIGHT)
			.build()
	)
	val painterRem by remember(posterThumbnail) {
		mutableStateOf(painter)
	}
	when (painterRem.state) {
		is AsyncImagePainter.State.Success -> Card(
			modifier = modifier
				.padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 24.dp)
				.width(120.dp)
				.height(IMAGE_REQUEST_WIDTH.dp),
			shape = RoundedCornerShape(8.dp),
			elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),
		) {
			Image(
				painter = painterRem,
				contentDescription = "",
				modifier = Modifier.fillMaxHeight(),
				contentScale = ContentScale.Crop
			)
		}

		is AsyncImagePainter.State.Error ->
			Box(
				modifier = Modifier
					.padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 24.dp)
					.width(120.dp)
					.height(IMAGE_REQUEST_WIDTH.dp),
			) {
				Icon(
					modifier = Modifier
						.align(Alignment.Center),
					painter = painterResource(id = R.drawable.ic_movie),
					contentDescription = null,
				)
			}

		is AsyncImagePainter.State.Loading -> Box(
			modifier = Modifier
				.padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 24.dp)
				.width(120.dp)
				.height(IMAGE_REQUEST_WIDTH.dp),
		) {
			CircularProgressIndicator(
				modifier = Modifier
					.align(Alignment.Center),
				color = Colors.primary
			)
		}

		else -> Unit
	}
}
