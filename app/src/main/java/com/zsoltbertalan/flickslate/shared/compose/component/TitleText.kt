package com.zsoltbertalan.flickslate.shared.compose.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.shared.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.compose.design.FixedColors
import com.zsoltbertalan.flickslate.shared.compose.design.titleMediumBold

@Composable
fun TitleText(title: String, modifier: Modifier = Modifier, isFirst: Boolean = false) {
	Text(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 8.dp),
		text = title,
		maxLines = 2,
		overflow = TextOverflow.Ellipsis,
		color = if (isFirst) FixedColors.onQuinaryFixed else Colors.onSurface,
		style = MaterialTheme.typography.titleMediumBold
	)
}
