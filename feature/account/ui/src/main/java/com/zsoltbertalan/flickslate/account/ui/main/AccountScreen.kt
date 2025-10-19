package com.zsoltbertalan.flickslate.account.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zsoltbertalan.flickslate.shared.domain.model.Account
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors

@Composable
fun AccountScreen(
	account: Account?,
	login: (String, String) -> Unit,
	logout: () -> Unit,
	navigateToMovieDetails: (Int) -> Unit,
	navigateToTvShowDetails: (Int) -> Unit,
	navigateToTvSeasonDetails: (Int, Int, Int) -> Unit,
	modifier: Modifier = Modifier,
) {
	if (account != null) {
		LoggedInComponent(
			Colors,
			account,
			logout,
			navigateToMovieDetails,
			navigateToTvShowDetails,
			navigateToTvSeasonDetails,
			modifier,
		)
	} else {
		LoginComponent(Colors, modifier, login)
	}
}
