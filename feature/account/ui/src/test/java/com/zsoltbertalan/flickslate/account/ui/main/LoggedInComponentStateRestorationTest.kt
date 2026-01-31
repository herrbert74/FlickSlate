package com.zsoltbertalan.flickslate.account.ui.main

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zsoltbertalan.flickslate.shared.domain.model.Account
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.navigation.LocalResultStore
import com.zsoltbertalan.flickslate.shared.ui.navigation.rememberResultStore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoggedInComponentStateRestorationTest {

	@get:Rule
	val composeTestRule = createComposeRule()

	@Test
	fun whenFavoritesTabSelected_itIsRestoredAfterStateRestoration() {
		val stateRestorationTester = StateRestorationTester(composeTestRule)

		stateRestorationTester.setContent {
			val resultStore = rememberResultStore()
			CompositionLocalProvider(LocalResultStore provides resultStore) {
				LoggedInComponent(
					colorScheme = Colors,
					account = Account(
						username = "john.doe",
						displayName = "John Doe",
						language = "en-US",
						region = "US",
						id = 12345,
						includeAdult = false,
					),
					logout = {},
					navigateToMovieDetails = {},
					navigateToTvShowDetails = {},
					navigateToTvEpisodeDetails = { _, _, _ -> },
					ratingsViewModel = null,
					favoritesViewModel = null,
				)
			}
		}

		composeTestRule.onNodeWithText("Favorites").performClick()
		composeTestRule.onNodeWithText("Favorites").assertIsSelected()

		stateRestorationTester.emulateSavedInstanceStateRestore()

		composeTestRule.onNodeWithText("Favorites").assertIsSelected()
	}

	@Test
	fun defaultTabIsRatings_andItRemainsRatingsAfterStateRestorationWithoutUserChange() {
		val stateRestorationTester = StateRestorationTester(composeTestRule)

		stateRestorationTester.setContent {
			val resultStore = rememberResultStore()
			CompositionLocalProvider(LocalResultStore provides resultStore) {
				LoggedInComponent(
					colorScheme = Colors,
					account = Account(
						username = "john.doe",
						displayName = "John Doe",
						language = "en-US",
						region = "US",
						id = 12345,
						includeAdult = false,
					),
					logout = {},
					navigateToMovieDetails = {},
					navigateToTvShowDetails = {},
					navigateToTvEpisodeDetails = { _, _, _ -> },
					ratingsViewModel = null,
					favoritesViewModel = null,
				)
			}
		}

		composeTestRule.onNodeWithText("Ratings").assertIsSelected()

		stateRestorationTester.emulateSavedInstanceStateRestore()

		composeTestRule.onNodeWithText("Ratings").assertIsSelected()
	}
}
