package com.zsoltbertalan.flickslate.shared.ui.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.shared.ui.R
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors

@Composable
fun ShowLoading(modifier: Modifier = Modifier, text: String = stringResource(id = R.string.loading)) {
	Column(
		modifier = modifier.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
	) {
		Text(text = text, color = Colors.onSurface)
		Spacer(modifier = Modifier.height(4.dp))
		CircularProgressIndicator(color = Colors.primary)
	}
}
