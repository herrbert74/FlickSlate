package com.zsoltbertalan.flickslate.account.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zsoltbertalan.flickslate.shared.compose.component.ListTitle
import com.zsoltbertalan.flickslate.shared.compose.component.autosizetext.AutoSizeText
import com.zsoltbertalan.flickslate.shared.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.compose.design.FlickSlateTheme
import com.zsoltbertalan.flickslate.shared.model.Account

@Composable
fun LoggedInComponent(
	colorScheme: ColorScheme,
	account: Account,
	modifier: Modifier = Modifier,
	logout: () -> Unit
) {
	Column(
		modifier = modifier
			.fillMaxSize()
			.background(colorScheme.surface)
			.padding(16.dp)
	) {
		ListTitle("Welcome")

		AutoSizeText(
			text = account.name,
			maxTextSize = 64.sp,
			alignment = Alignment.Center,
			maxLines = 1,
		)

		Button(logout) {
			Text("Logout")
		}
	}
}

@Preview
@Composable
private fun PreviewAutoSizeTextWithMaxLinesSetToOne() {
	FlickSlateTheme {
		LoggedInComponent(
			modifier = Modifier.fillMaxSize(),
			colorScheme = Colors,
			account = Account(name = "John Doe"),
			logout = {},
		)
	}
}
