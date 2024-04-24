package com.zsoltbertalan.flickslate.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.design.LocalAppColors
import com.zsoltbertalan.flickslate.design.LocalFixedColors
import com.zsoltbertalan.flickslate.design.titleMediumBold

@Composable
fun TitleText(modifier: Modifier = Modifier, title: String, isFirst: Boolean = false) {
	Text(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 8.dp),
		text = title,
		maxLines = 2,
		overflow = TextOverflow.Ellipsis,
		color = if (isFirst) LocalFixedColors.current.onQuinaryFixed else LocalAppColors.current.onSurface,
		style = MaterialTheme.typography.titleMediumBold
	)
}
