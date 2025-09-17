package com.zsoltbertalan.flickslate.account.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zsoltbertalan.flickslate.shared.domain.model.Account
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Dimens
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FlickSlateTheme
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FlickSlateTypography
import com.zsoltbertalan.flickslate.shared.ui.compose.design.titleMediumBold

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
		BasicText(
			text = "Welcome ${account.displayName}",
			style = FlickSlateTypography.titleMediumBold,
			maxLines = 1,
			autoSize = TextAutoSize.StepBased(maxFontSize = 32.sp),
			modifier = Modifier.fillMaxWidth().padding(top = Dimens.marginLarge, bottom = Dimens.marginExtraLarge)
		)

		// Display other account details
		AccountDetailRow(label = "Username:", value = account.username)
		AccountDetailRow(label = "User ID:", value = account.id.toString())
		AccountDetailRow(label = "Language:", value = account.language)
		AccountDetailRow(label = "Include Adult Content:", value = if (account.includeAdult) "Yes" else "No")

		Spacer(modifier = Modifier.height(Dimens.marginExtraLarge))

		Button(logout) {
			Text("Logout")
		}
	}
}


@Composable
private fun AccountDetailRow(label: String, value: String) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 4.dp),
		horizontalArrangement = Arrangement.SpaceBetween // Arrange label and value on opposite ends
	) {
		Text(
			text = label,
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
		Text(
			text = value,
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onSurface
		)
	}
}

@Preview
@Composable
private fun PreviewAutoSizeTextWithMaxLinesSetToOne() {
	FlickSlateTheme {
		LoggedInComponent(
			modifier = Modifier.fillMaxSize(),
			colorScheme = Colors,
			account = Account(
				username = "john.doe",
				displayName = "John Doe",
				language = "en-US",
				id = 12345,
				includeAdult = false,
			),
			logout = {},
		)
	}
}
