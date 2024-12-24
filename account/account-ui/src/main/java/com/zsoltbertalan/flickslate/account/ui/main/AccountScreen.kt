package com.zsoltbertalan.flickslate.account.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zsoltbertalan.flickslate.shared.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.model.Account

@Composable
fun AccountScreen(
	account: Account?,
	login: (String, String) -> Unit,
	modifier: Modifier = Modifier,
) {
	if (account != null) {
		LoggedInComponent(Colors, account, modifier)
	} else {
		LoginComponent(Colors, modifier, login)
	}
}

