package com.zsoltbertalan.flickslate

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zsoltbertalan.flickslate.account.ui.favorites.FavoritesScreen
import com.zsoltbertalan.flickslate.account.ui.favorites.FavoritesViewModel
import com.zsoltbertalan.flickslate.shared.ui.compose.waitUntilAtLeastOneExistsCopy
import com.zsoltbertalan.flickslate.shared.ui.navigation.LocalResultStore
import com.zsoltbertalan.flickslate.shared.ui.navigation.rememberResultStore
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.metroViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritesScreenTest {

	@get:Rule
	val composeTestRule = createAndroidComposeRule<ComponentActivity>()

	@Before
	fun setUp() {
		AccountListTestState.resetToDefaults()
	}

	@Test
	fun favoritesScreen_whenLoaded_showsFavoriteContent() {
		with(composeTestRule) {
			val metroVmf = (activity.application as FlickSlateApp).appGraph.metroViewModelFactory
			setContent {
				CompositionLocalProvider(LocalMetroViewModelFactory provides metroVmf) {
					val resultStore = rememberResultStore()
					val viewModel = metroViewModel<FavoritesViewModel>()
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
			}

			waitUntilAtLeastOneExistsCopy(hasText("Brazil"), 5000L)

			onNodeWithText("Brazil", useUnmergedTree = true).assertIsDisplayed()
			onNodeWithText("Detectorists", useUnmergedTree = true).assertIsDisplayed()
		}
	}
}
