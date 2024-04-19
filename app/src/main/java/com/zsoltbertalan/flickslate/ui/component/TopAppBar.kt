package com.zsoltbertalan.flickslate.ui.component

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.zsoltbertalan.flickslate.R
import com.zsoltbertalan.flickslate.design.Colors
import com.zsoltbertalan.flickslate.design.FlickSlateTheme
import com.zsoltbertalan.flickslate.design.FlickSlateTypography

@Composable
fun FlickSlateTopAppBar(
	modifier: Modifier = Modifier,
	title: String = stringResource(id = R.string.app_name),
) {
	TopAppBar(
		title = {
			Text(
				style = FlickSlateTypography.headlineMedium,
				color = Colors.onSurface,
				text = title,
				fontSize = 22.sp,
			)
		},
		colors = TopAppBarDefaults.topAppBarColors(
			containerColor = Colors.background
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
