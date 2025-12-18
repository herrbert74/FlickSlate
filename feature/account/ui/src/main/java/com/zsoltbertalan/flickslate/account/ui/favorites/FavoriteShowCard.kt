package com.zsoltbertalan.flickslate.account.ui.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.shared.ui.compose.component.PosterImage
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors

val IMAGE_WIDTH = 100.dp
val IMAGE_HEIGHT = 150.dp
private const val IMAGE_REQUEST_WIDTH = 150
private const val IMAGE_REQUEST_HEIGHT = 225

@Composable
fun FavoriteShowCard(
	title: String,
	posterPath: String?,
	modifier: Modifier = Modifier,
) {
	Card(
		modifier = modifier.width(IMAGE_WIDTH),
		shape = MaterialTheme.shapes.large,
		colors = CardDefaults.cardColors(
			containerColor = Colors.surfaceContainerHighest,
		)
	) {
		Column {
			PosterImage(
				posterThumbnail = posterPath,
				imageWidth = IMAGE_REQUEST_WIDTH,
				imageHeight = IMAGE_REQUEST_HEIGHT,
				modifier = Modifier.height(IMAGE_HEIGHT)
			)
			Text(
				text = title,
				style = MaterialTheme.typography.titleSmall,
				minLines = 2,
				maxLines = 2,
				overflow = TextOverflow.Ellipsis,
				modifier = Modifier.padding(4.dp),
			)
		}
	}
}
