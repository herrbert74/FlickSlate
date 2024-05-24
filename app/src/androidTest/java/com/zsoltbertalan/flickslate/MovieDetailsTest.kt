package com.zsoltbertalan.flickslate

import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zsoltbertalan.flickslate.di.IoDispatcher
import com.zsoltbertalan.flickslate.di.MainDispatcher
import com.zsoltbertalan.flickslate.domain.api.MoviesRepository
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

	@get:Rule
	val composeTestRule = createComposeRule()

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

		composeTestRule.waitUntilAtLeastOneExists(hasTestTag("MoviesHeader"), 1000L)

		composeTestRule.onNodeWithText("Affenpinscher", ignoreCase = true).assertExists()

	}

	@Test
	fun showMovieImages() {

		composeTestRule.waitUntilAtLeastOneExists(hasTestTag("MoviesHeader"), 3000L)

		composeTestRule.onNodeWithText("Affenpinscher", ignoreCase = true).performClick()

		composeTestRule.waitUntilAtLeastOneExists(hasTestTag("MovieRow"), 3000L)

		composeTestRule.onAllNodesWithTag("MovieRow").assertAny(hasTestTag("MovieRow"))

	}

}