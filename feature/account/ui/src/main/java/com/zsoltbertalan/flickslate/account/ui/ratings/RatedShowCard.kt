package com.zsoltbertalan.flickslate.account.ui.ratings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.shared.ui.compose.component.PosterImage
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FlickSlateTheme

private val IMAGE_WIDTH = 80.dp
private val IMAGE_HEIGHT = 120.dp
private const val IMAGE_REQUEST_WIDTH = 120
private const val IMAGE_REQUEST_HEIGHT = 180

@Composable
fun RatedShowCard(
	title: String,
	posterPath: String?,
	rating: Float,
	modifier: Modifier = Modifier,
) {
	Card(
		modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
		shape = MaterialTheme.shapes.large,
		colors = CardDefaults.cardColors(
			containerColor = Colors.surfaceContainerHighest,
		)
	) {
		Row(modifier = Modifier.height(IMAGE_HEIGHT)) {
			PosterImage(
				posterThumbnail = posterPath,
				imageWidth = IMAGE_REQUEST_WIDTH,
				imageHeight = IMAGE_REQUEST_HEIGHT,
				modifier = Modifier.width(IMAGE_WIDTH)
			)
			Column(
				modifier = Modifier
					.padding(start = 12.dp, top = 8.dp)
					.align(Alignment.CenterVertically)
			) {
				Text(
					text = title,
					style = MaterialTheme.typography.titleMedium,
				)

				Text(
					text = "Your rating: $rating",
					style = MaterialTheme.typography.bodyMedium,
					modifier = Modifier.padding(top = 4.dp)
				)
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
private fun RatedShowCardPreview() {
	FlickSlateTheme {
		RatedShowCard(
			title = "Sample Movie",
			posterPath = null,
			rating = 8.5f
		)
	}
}
