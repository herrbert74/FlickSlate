package com.zsoltbertalan.flickslate.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.design.Colors

@Composable
fun ListTitle(modifier: Modifier = Modifier, @StringRes titleId: Int) {
	val titleRemember by remember {
		mutableIntStateOf(titleId)
	}
	Text(
		modifier = modifier
			.padding(16.dp),
		style = MaterialTheme.typography.titleMedium,
		text = stringResource(id = titleRemember),
		maxLines = 1,
		color = Colors.onBackground,
	)
}

@Composable
fun ListTitle(modifier: Modifier = Modifier, title: String) {
	val titleRemember by remember {
		mutableStateOf(title)
	}
	Text(
		modifier = modifier
			.fillMaxWidth()
			.padding(16.dp),
		style = MaterialTheme.typography.titleMedium,
		text = titleRemember,
		maxLines = 1,
		color = Colors.onBackground,
	)
}
