package com.zsoltbertalan.flickslate.shared.compose.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zsoltbertalan.flickslate.shared.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.compose.design.FixedColors

@Composable
fun RatingText(rating: Float, modifier: Modifier = Modifier, isFirst: Boolean = false) {
	Text(
		modifier = modifier
			.fillMaxWidth()
			.padding(end = 8.dp, top = 8.dp),
		text = rating.toString(),
		textAlign = TextAlign.End,
		fontSize = 14.sp,
		maxLines = 1,
		style = MaterialTheme.typography.headlineSmall,
		color = if (isFirst) FixedColors.tertiaryFixed else Colors.tertiary,
	)
}
