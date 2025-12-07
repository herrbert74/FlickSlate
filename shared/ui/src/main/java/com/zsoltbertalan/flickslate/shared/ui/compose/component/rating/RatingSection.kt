package com.zsoltbertalan.flickslate.shared.ui.compose.component.rating

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.shared.ui.compose.component.TitleText

@Composable
fun RatingSection(
	title: String,
	titleTestTag: String,
	initialRating: Float,
	isRated: Boolean,
	isRatingInProgress: Boolean,
	ratingLabelProvider: (Float) -> String,
	rateLabel: String,
	changeLabel: String,
	deleteLabel: String,
	onRate: (Float) -> Unit,
	onChange: (Float) -> Unit,
	onDelete: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(modifier = modifier.padding(horizontal = 16.dp)) {
		TitleText(
			modifier = Modifier
				.padding(vertical = 16.dp)
				.testTag(titleTestTag),
			title = title
		)
		var sliderPosition by remember(initialRating) { mutableFloatStateOf(initialRating) }
		Slider(
			value = sliderPosition,
			onValueChange = { sliderPosition = it },
			valueRange = 0f..10f,
			steps = 9,
			enabled = !isRatingInProgress,
			modifier = Modifier.testTag("Rating Slider"),
		)
		Text(
			text = ratingLabelProvider(sliderPosition),
			modifier = Modifier.testTag("Rating Text")
		)
		Row(modifier = Modifier.padding(top = 8.dp)) {
			Button(
				onClick = {
					if (isRated) {
						onChange(sliderPosition)
					} else {
						onRate(sliderPosition)
					}
				},
				enabled = !isRatingInProgress,
				modifier = Modifier.testTag("Rate Button")
			) {
				Text(text = if (isRated) changeLabel else rateLabel)
			}
			if (isRated) {
				Spacer(modifier = Modifier.padding(horizontal = 8.dp))
				OutlinedButton(
					onClick = onDelete,
					enabled = !isRatingInProgress,
					modifier = Modifier.testTag("Delete Rating Button")
				) {
					Text(text = deleteLabel)
				}
			}
		}
	}
}
