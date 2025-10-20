package com.zsoltbertalan.flickslate

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zsoltbertalan.flickslate.account.ui.ratings.RatingsScreen
import com.zsoltbertalan.flickslate.account.ui.ratings.RatingsViewModel
import com.zsoltbertalan.flickslate.shared.ui.compose.waitUntilAtLeastOneExistsCopy
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class RatingsScreenTest {

	@get:Rule(order = 0)
	val hiltAndroidRule = HiltAndroidRule(this)

	@get:Rule(order = 1)
	val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

	@Before
	fun setUp() {
		hiltAndroidRule.inject()
	}

	@Test
	fun ratingsScreen_whenLoaded_showsRatedContent() {
		with(composeTestRule) {
			setContent {
				// Assuming Hilt is configured to provide a ViewModel that will
				// return a Success state with mock data for this test.
				RatingsScreen(
					navigateToMovieDetails = { },
					navigateToTvShowDetails = { },
					navigateToTvEpisodeDetails = { _, _, _ -> },
					uiState = hiltViewModel<RatingsViewModel>().uiState.collectAsState()
				)
			}

			// Wait for the content to be loaded and displayed
			waitUntilAtLeastOneExistsCopy(hasText("Detectorists"), 5000L)

			// Assert that items from each category are displayed
			onNodeWithText("Brazil", useUnmergedTree = true).assertIsDisplayed()
			onNodeWithText("Detectorists", useUnmergedTree = true).assertIsDisplayed()
			onNodeWithText("Episode 1", useUnmergedTree = true).assertIsDisplayed()
		}
	}
}
