package com.zsoltbertalan.flickslate

import androidx.compose.ui.test.SemanticsMatcher
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
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MovieDetailsTest {

	@get:Rule(order = 0)
	val hiltAndroidRule = HiltAndroidRule(this)

	@get:Rule(order = 1)
	val composeTestRule = createAndroidComposeRule<FlickSlateActivity>()

	@Before
	fun setUp() {

		hiltAndroidRule.inject()

	}

	@Test
	fun showMovies() {

		composeTestRule.onRoot(useUnmergedTree = true).printToLog("showMovies")

		waitUntilAtLeastOneExists(hasTestTag("MovieColumn"), 1000L)

		composeTestRule.onNodeWithText("name1", useUnmergedTree = true).assertExists()

	}

	@Test
	fun showMovieDetails() {

		waitUntilAtLeastOneExists(hasTestTag("MovieColumn"), 1000L)

		composeTestRule.onNodeWithText("name1", ignoreCase = true).performClick()

		waitUntilAtLeastOneExists(hasText("Brazil"), 5000L)

		composeTestRule.onAllNodesWithText("Brazil", useUnmergedTree = true).assertAny(hasText("Brazil"))

	}

	/**
	 * Copied from [androidx.compose.ui.test.ComposeUiTest] to override useUnmergedTree.
	 * The override is only available for onXXX matchers.
	 */
	private fun waitUntilAtLeastOneExists(
		matcher: SemanticsMatcher,
		timeoutMillis: Long = 1_000L
	) {
		composeTestRule.waitUntil(timeoutMillis) {
			composeTestRule.onAllNodes(matcher, true).fetchSemanticsNodes().isNotEmpty()
		}
	}

}
