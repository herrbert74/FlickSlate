package com.zsoltbertalan.flickslate.shared.ui.compose.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.zsoltbertalan.flickslate.shared.ui.R
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FlickSlateTheme
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FlickSlateTypography

@Composable
fun FlickSlateTopAppBar(
	popBackStack: () -> Boolean,
	backgroundColor: Color,
	modifier: Modifier = Modifier,
	title: String = stringResource(id = R.string.app_name),
	showBack: Boolean = false,
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
			containerColor = backgroundColor,
		),
		navigationIcon = {
			if (showBack) {
				IconButton(onClick = { popBackStack() }) {
					Icon(
						imageVector = Icons.AutoMirrored.Filled.ArrowBack,
						contentDescription = "Finish",
						tint = Colors.onSurface
					)
				}
			}
		},
		modifier = modifier,
	)
}

@Preview(showBackground = true)
@Composable
internal fun TopAppBarPreview() {
	FlickSlateTheme {
		FlickSlateTopAppBar(
			{ true },
			backgroundColor = Colors.surface,
			title = stringResource(id = R.string.app_name)
		)
	}
}

private const val MOVIE_DETAILS_PREVIEW_BACKGROUND_COLOR = 0xFFa4e5e8

@Preview(showBackground = true)
@Composable
internal fun TopAppBarPreviewShowBack() {
	FlickSlateTheme {
		FlickSlateTopAppBar(
			{ true },
			backgroundColor = Color(MOVIE_DETAILS_PREVIEW_BACKGROUND_COLOR),
			title = "Brazil",
			showBack = true
		)
	}
}
