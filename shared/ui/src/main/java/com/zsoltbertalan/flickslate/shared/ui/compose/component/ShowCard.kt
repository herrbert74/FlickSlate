package com.zsoltbertalan.flickslate.shared.ui.compose.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.shared.domain.model.MovieCardType
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FixedColors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FlickSlateTheme
import com.zsoltbertalan.flickslate.shared.ui.compose.util.movieCardWidth
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private val IMAGE_REQUEST_WIDTH = 200.dp
private const val IMAGE_REQUEST_HEIGHT = 380

@Composable
fun ShowCard(
	title: String?,
	voteAverage: Float?,
	overview: String?,
	releaseDate: String?,
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
			releaseDate = releaseDate,
			cardType = cardType,
			isFirst = isFirst,
		)
		posterPath?.let {
			ThumbnailCard(
				posterThumbnail = it,
				width = 120.dp,
				height = IMAGE_REQUEST_WIDTH,
				imageWidth = IMAGE_REQUEST_WIDTH.value.toInt(),
				imageHeight = IMAGE_REQUEST_HEIGHT
			)
		}
		voteAverage?.let {
			FloatingRatingCircle(
				rating = it,
				modifier = Modifier
					.align(androidx.compose.ui.Alignment.TopEnd)
					.offset(y = 13.dp)
			)
		}
	}
}

@Composable
fun ShowDetailCard(
	title: String?,
	overview: String?,
	releaseDate: String?,
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
			if (isFirst) {
				CardDefaults.cardColors(containerColor = FixedColors.quinaryFixed)
			} else {
				CardDefaults.cardColors(containerColor = Colors.surfaceContainerHighest)
			}
	) {
		Box {
			Column(
				modifier = Modifier
					.movieCardWidth(cardType)
					.padding(start = 140.dp)
					.testTag("MovieColumn")
			) {
				title?.let {
					TitleText(title = it, isFirst = isFirst)
				}
				overview?.let {
					DescriptionText(description = it, isFirst = isFirst)
				}
				releaseDate?.let {
					NoteText(description = it, isFirst = isFirst)
				}
			}
		}
	}
}

@Preview
@Composable
private fun ShowCardRatingsPreview(@PreviewParameter(RatingProvider::class) rating: Float) {
	FlickSlateTheme {
		ShowCard(
			title = "Brazil",
			overview = "This is a good film",
			releaseDate = "1985-02-20",
			voteAverage = rating,
			posterPath = null,
			cardType = MovieCardType.FULL,
		)
	}
}

class RatingProvider : PreviewParameterProvider<Float> {

	override val values = sequenceOf(LOW_RATING, MEDIUM_RATING, HIGH_RATING)

	override fun getDisplayName(index: Int): String {
		return "Rating - ${listOf("Low", "Medium", "High")[index]}"
	}

	companion object {

		private const val LOW_RATING = 4.5f
		private const val MEDIUM_RATING = 5.7f
		private const val HIGH_RATING = 7.8f
	}
}

@Preview
@Composable
private fun ShowCardTitleAndDescriptionPreview(
	@PreviewParameter(TitleAndDescriptionProvider::class) strings: ImmutableList<String>
) {
	FlickSlateTheme {
		ShowCard(
			title = strings[0],
			overview = strings[1],
			releaseDate = "1985-02-20",
			voteAverage = 7.89f,
			posterPath = null,
			cardType = MovieCardType.FULL,
		)
	}
}

class TitleAndDescriptionProvider : PreviewParameterProvider<ImmutableList<String>> {

	override val values = sequenceOf(
		persistentListOf(
			"Brazil",
			"This is a good film"
		),
		persistentListOf(
			"Brazil",
			"""This is a good film with a title that is too long to display in the description field, 
				|so it has to be cut off at one point."""
				.trimMargin()
		),
		persistentListOf(
			"The adventures of a hero that has a very long name",
			"""This is a good film with a title that is too long to display in the description field, 
				|so it has to be cut off at one point."""
				.trimMargin()
		),
	)

	override fun getDisplayName(index: Int): String {
		return listOf("Short", "Long description", "Long title and description")[index]
	}
}
