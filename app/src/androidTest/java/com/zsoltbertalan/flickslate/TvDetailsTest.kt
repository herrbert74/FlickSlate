package com.zsoltbertalan.flickslate

import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zsoltbertalan.flickslate.main.FlickSlateActivity
import com.zsoltbertalan.flickslate.shared.compose.waitUntilAtLeastOneExistsCopy
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TvDetailsTest {

	@get:Rule(order = 0)
	val hiltAndroidRule = HiltAndroidRule(this)

	@get:Rule(order = 1)
	val composeTestRule = createAndroidComposeRule<FlickSlateActivity>()

	@Before
	fun setUp() {
		hiltAndroidRule.inject()
	}

	@Test
	fun showTvTab() {
		with(composeTestRule) {
			onRoot(useUnmergedTree = true).printToLog("showTv")
			onNodeWithText("Tv").performClick()
			waitUntilAtLeastOneExistsCopy(hasTestTag("MovieColumn"), 1000L)

			onNodeWithText("Detectorists", useUnmergedTree = true).assertExists()
			onNodeWithText("Movies", useUnmergedTree = true).assertExists()
		}
	}

	@Test
	fun showTvDetails() {
		with(composeTestRule) {
			onNodeWithText("Tv").performClick()
			waitUntilAtLeastOneExistsCopy(hasTestTag("MovieColumn"), 1000L)
			onNodeWithText("Detectorists", ignoreCase = true).performClick()
			waitUntilAtLeastOneExistsCopy(hasText("Detectorists"), 5000L)

			onAllNodesWithText("Detectorists", useUnmergedTree = true).assertAny(hasText("Detectorists"))
			onNodeWithText("Movies", useUnmergedTree = true).assertDoesNotExist()
			onNodeWithText("Season 1", useUnmergedTree = true).assertExists()

			composeTestRule.activityRule.scenario.onActivity { activity ->
				activity.onBackPressedDispatcher.onBackPressed()
			}

			onNodeWithText("Movies", useUnmergedTree = true).assertExists()
		}

	}

}
