package com.zsoltbertalan.flickslate.shared.compose.component

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.zsoltbertalan.flickslate.shared.R
import com.zsoltbertalan.flickslate.shared.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.compose.design.FlickSlateTheme
import com.zsoltbertalan.flickslate.shared.compose.design.FlickSlateTypography

@Composable
fun FlickSlateTopAppBar(
	modifier: Modifier = Modifier,
	title: String = stringResource(id = R.string.app_name),
) {
	TopAppBar(
		title = {
			Text(
				style = FlickSlateTypography.titleLarge,
				color = Colors.onSurface,
				text = title,
			)
		},
		colors = TopAppBarDefaults.topAppBarColors(
			containerColor = Colors.surface
		),
		modifier = modifier,
	)
}

@Preview(showBackground = true)
@Composable
private fun TopAppBarPreview() {
	FlickSlateTheme {
		FlickSlateTopAppBar()
	}
}
