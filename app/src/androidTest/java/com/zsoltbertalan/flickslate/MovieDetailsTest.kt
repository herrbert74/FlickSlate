package com.zsoltbertalan.flickslate

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zsoltbertalan.flickslate.common.async.IoDispatcher
import com.zsoltbertalan.flickslate.common.async.MainDispatcher
import com.zsoltbertalan.flickslate.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.presentation.ui.FlickSlateActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MovieDetailsTest {

	@get:Rule(order = 0)
	val hiltAndroidRule = HiltAndroidRule(this)

	@get:Rule(order = 1)
	val composeTestRule = createAndroidComposeRule<FlickSlateActivity>()

	@Inject
	lateinit var moviesRepository: MoviesRepository

	@Inject
	@MainDispatcher
	lateinit var mainContext: CoroutineDispatcher

	@Inject
	@IoDispatcher
	lateinit var ioContext: CoroutineDispatcher

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

	/**
	 * This fails currently due to a strange problem with Kotlin Value classes(?).
	 * https://github.com/michaelbull/kotlin-result/issues/100
	 * https://youtrack.jetbrains.com/issue/KT-53559/JVM-ClassCastException-class-kotlin.Result-cannot-be-cast-to-class-java.lang.String-with-Retrofit
	 *
	 * The Ok Result returned from the mock Repo is wrapped again in Ok, so it returns Ok(Ok(...))
	 * But only in UI tests, where the mock is in a lambda... The variable in the debug window is fine, but not the
	 * overlay data, when you hover over it.
	 * Might be a problem with JVM through JUnit?
	 */
	@Test
	fun showMovieDetails() {

		waitUntilAtLeastOneExists(hasTestTag("MovieColumn"), 1000L)

		composeTestRule.onNodeWithText("name1", ignoreCase = true).performClick()

		waitUntilAtLeastOneExists(hasTestTag("MovieRow"), 1000L)

		composeTestRule.onAllNodesWithTag("MovieRow").assertAny(hasTestTag("MovieRow"))

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
