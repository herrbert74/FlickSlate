package com.zsoltbertalan.flickslate.ui.component

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.domain.model.MovieCardType
import com.zsoltbertalan.flickslate.ext.movieCardWidth

//TODO Is this file representing a TitleCard?
@Composable
fun TitleCard(
	modifier: Modifier,
	title: String?,
	voteAverage: Float?,
	overview: String?,
	posterPath: String?,
	cardType: MovieCardType = MovieCardType.FULL,
) {
	Box(
		modifier = modifier.padding(horizontal = 16.dp)
	) {
		TitleDetailCard(
			name = title,
			description = overview,
			rating = voteAverage,
			cardType = cardType
		)
		posterPath?.let {
			ThumbnailCard(posterThumbnail = it)
		}
	}
}

@Composable
fun TitleDetailCard(
	modifier: Modifier = Modifier,
	name: String?,
	description: String?,
	rating: Float?,
	cardType: MovieCardType
) {
	Card(
		modifier = modifier
            .fillMaxWidth()
            .padding(top = 45.dp)
            .height(200.dp),
		shape = RoundedCornerShape(8.dp),
		elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
		colors = CardDefaults.cardColors(containerColor = Color.White)
	) {
		Column(
			modifier = Modifier
                .movieCardWidth(cardType)
                .padding(start = 140.dp)
		) {
			rating?.let {
				RatingText(rating = it)
			}
			name?.let {
				TitleText(title = it)
			}
			description?.let {
				DescriptionText(description = it)
			}
		}
	}
}
