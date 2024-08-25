package com.zsoltbertalan.flickslate.shared.compose.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.shared.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.compose.design.FixedColors
import com.zsoltbertalan.flickslate.shared.compose.design.FlickSlateTheme
import com.zsoltbertalan.flickslate.movies.domain.model.MovieCardType
import com.zsoltbertalan.flickslate.shared.compose.util.movieCardWidth

@Composable
fun ShowCard(
	title: String?,
	voteAverage: Float?,
	overview: String?,
	posterPath: String?,
	modifier: Modifier = Modifier,
	cardType: MovieCardType = MovieCardType.FULL,
	isFirst: Boolean = false,
) {
	Box(
		modifier = modifier
			.padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 0.dp)

	) {
		ShowDetailCard(
			title = title,
			overview = overview,
			voteAverage = voteAverage,
			cardType = cardType,
			isFirst = isFirst,
		)
		posterPath?.let {
			ThumbnailCard(posterThumbnail = it)
		}
	}
}

@Composable
fun ShowDetailCard(
	title: String?,
	overview: String?,
	voteAverage: Float?,
	cardType: MovieCardType,
	modifier: Modifier = Modifier,
	isFirst: Boolean = false,
) {
	Card(
		modifier = modifier
			.fillMaxWidth()
			.padding(top = 45.dp)
			.height(200.dp),
		shape = RoundedCornerShape(8.dp),
		elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
		colors =
		if (isFirst)
			CardDefaults.cardColors(containerColor = FixedColors.quinaryFixed)
		else
			CardDefaults.cardColors(containerColor = Colors.surfaceContainerHighest)
	) {
		Column(
			modifier = Modifier
				.movieCardWidth(cardType)
				.padding(start = 140.dp)
				.testTag("MovieColumn")
		) {
			voteAverage?.let {
				RatingText(rating = it, isFirst = isFirst)
			}
			title?.let {
				TitleText(title = it, isFirst = isFirst)
			}
			overview?.let {
				DescriptionText(description = it, isFirst = isFirst)
			}
		}
	}
}

@Preview
@Composable
private fun ShowDetailCardPreview() {
	FlickSlateTheme {
		ShowDetailCard(
			title = "Brazil",
			overview = "This is a good film",
			voteAverage = 6.999f,
			cardType = MovieCardType.FULL,
		)
	}
}
