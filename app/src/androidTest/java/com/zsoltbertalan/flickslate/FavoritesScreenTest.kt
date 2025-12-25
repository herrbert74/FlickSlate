package com.zsoltbertalan.flickslate

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zsoltbertalan.flickslate.account.ui.favorites.FavoritesScreen
import com.zsoltbertalan.flickslate.account.ui.favorites.FavoritesViewModel
import com.zsoltbertalan.flickslate.shared.ui.compose.waitUntilAtLeastOneExistsCopy
import com.zsoltbertalan.flickslate.shared.ui.navigation.LocalResultStore
import com.zsoltbertalan.flickslate.shared.ui.navigation.rememberResultStore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class FavoritesScreenTest {

	@get:Rule(order = 0)
	val hiltAndroidRule = HiltAndroidRule(this)

	@get:Rule(order = 1)
	val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

	@Before
	fun setUp() {
		hiltAndroidRule.inject()
		AccountListTestState.resetToDefaults()
	}

	@Test
	fun favoritesScreen_whenLoaded_showsFavoriteContent() {
		with(composeTestRule) {
			setContent {
				val resultStore = rememberResultStore()
				val viewModel = hiltViewModel<FavoritesViewModel>()
				CompositionLocalProvider(LocalResultStore provides resultStore) {
					FavoritesScreen(
						favoriteMovies = viewModel.favoriteMoviesPaginationState,
						favoriteTvShows = viewModel.favoriteTvShowsPaginationState,
						navigateToMovieDetails = { },
						navigateToTvShowDetails = { },
						onRefresh = viewModel::refresh,
					)
				}
			}

			waitUntilAtLeastOneExistsCopy(hasText("Brazil"), 5000L)

			onNodeWithText("Brazil", useUnmergedTree = true).assertIsDisplayed()
			onNodeWithText("Detectorists", useUnmergedTree = true).assertIsDisplayed()
		}
	}
}
