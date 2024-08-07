package com.zsoltbertalan.flickslate.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zsoltbertalan.flickslate.presentation.design.Colors
import com.zsoltbertalan.flickslate.presentation.design.FixedColors

@Composable
fun DescriptionText(description: String, modifier: Modifier = Modifier, isFirst: Boolean = false) {
	Text(
		modifier = modifier
			.fillMaxWidth()
			.padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 24.dp),
		text = description,
		maxLines = 3,
		style = MaterialTheme.typography.bodyLarge,
		color = if (isFirst) FixedColors.onQuinaryFixed else Colors.onSurface,
		overflow = TextOverflow.Ellipsis,
		fontSize = 14.sp,
	)
}
