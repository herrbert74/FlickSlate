package com.zsoltbertalan.flickslate.shared.ui.compose.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

const val BASE_IMAGE_PATH = "https://image.tmdb.org/t/p/original"

@Composable
fun ThumbnailCard(
	posterThumbnail: String,
	modifier: Modifier = Modifier,
	width: Dp,
	height: Dp,
	imageWidth: Int,
	imageHeight: Int
) {
	Card(
		modifier = modifier
			.padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 24.dp)
			.width(width)
			.height(height),
		shape = RoundedCornerShape(8.dp),
		elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),
	) {
		PosterImage(
			posterThumbnail = posterThumbnail,
			imageWidth = imageWidth,
			imageHeight = imageHeight
		)
	}
}
