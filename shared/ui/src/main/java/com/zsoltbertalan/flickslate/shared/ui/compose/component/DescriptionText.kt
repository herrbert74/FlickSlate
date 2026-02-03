package com.zsoltbertalan.flickslate.shared.ui.compose.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FixedColors

@Composable
fun DescriptionText(description: String, modifier: Modifier = Modifier, isFirst: Boolean = false) {
	Text(
		modifier = modifier
			.fillMaxWidth()
			.padding(start = 8.dp, end = 8.dp, top = 0.dp, bottom = 8.dp),
		text = description,
		maxLines = 3,
		style = MaterialTheme.typography.bodyLarge,
		color = if (isFirst) FixedColors.onQuinaryFixed else Colors.onSurface,
		overflow = TextOverflow.Ellipsis,
	)
}
