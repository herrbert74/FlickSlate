package com.zsoltbertalan.flickslate

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zsoltbertalan.flickslate.account.ui.ratings.RatingsScreen
import com.zsoltbertalan.flickslate.account.ui.ratings.RatingsViewModel
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
class RatingsScreenTest {

	@get:Rule
	val composeTestRule = createAndroidComposeRule<ComponentActivity>()

	@Before
	fun setUp() {
		AccountListTestState.resetToDefaults()
	}

	@Test
	fun ratingsScreen_whenLoaded_showsRatedContent() {
		with(composeTestRule) {
			val metroVmf = (activity.application as FlickSlateApp).appGraph.metroViewModelFactory
			setContent {
				CompositionLocalProvider(LocalMetroViewModelFactory provides metroVmf) {
					val resultStore = rememberResultStore()
					val viewModel = metroViewModel<RatingsViewModel>()
					CompositionLocalProvider(LocalResultStore provides resultStore) {
						RatingsScreen(
							ratedMovies = viewModel.ratedMoviesPaginationState,
							ratedTvShows = viewModel.ratedTvShowsPaginationState,
							ratedTvEpisodes = viewModel.ratedTvEpisodesPaginationState,
							navigateToMovieDetails = { },
							navigateToTvShowDetails = { },
							navigateToTvEpisodeDetails = { _, _, _ -> },
							onRefresh = viewModel::refresh,
						)
					}
				}
			}

			waitUntilAtLeastOneExistsCopy(hasText("Detectorists"), 5000L)

			onNodeWithText("Brazil", useUnmergedTree = true).assertIsDisplayed()
			onNodeWithText("Detectorists", useUnmergedTree = true).assertIsDisplayed()

			onNodeWithTag("RatingsColumn").performScrollToNode(hasText("Episode 1"))
			waitUntilAtLeastOneExistsCopy(hasText("Episode 1"), 1000L)

			onNodeWithText("Episode 1", useUnmergedTree = true).assertIsDisplayed()
		}
	}
}
