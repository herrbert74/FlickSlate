package com.zsoltbertalan.flickslate

import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zsoltbertalan.flickslate.main.FlickSlateActivity
import com.zsoltbertalan.flickslate.movies.data.repository.AutoBindMovieRatingsAccessorActivityRetainedModule
import com.zsoltbertalan.flickslate.movies.domain.api.MovieRatingsRepository
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetailMother
import com.zsoltbertalan.flickslate.shared.ui.compose.waitUntilAtLeastOneExistsCopy
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(AutoBindMovieRatingsAccessorActivityRetainedModule::class)
@RunWith(AndroidJUnit4::class)
class MovieDetailsTest {

	@get:Rule(order = 0)
	val hiltAndroidRule = HiltAndroidRule(this)

	@get:Rule(order = 1)
	val composeTestRule = createAndroidComposeRule<FlickSlateActivity>()

	@BindValue
	val fakeRatingsRepository: MovieRatingsRepository = FakeMovieRatingsRepository()

	@Inject
	lateinit var fakeAccountRepository: FakeAccountRepository

	@Inject
	lateinit var fakeMoviesRepository: FakeMoviesRepository

	@Before
	fun setUp() {
		hiltAndroidRule.inject()
	}

	private fun navigateToMovieDetails() {
		composeTestRule.waitUntilAtLeastOneExistsCopy(hasTestTag("MovieColumn"), 1000L)
		composeTestRule.onNodeWithText("name1", ignoreCase = true).performClick()
		composeTestRule.waitUntilAtLeastOneExistsCopy(hasText("Brazil"), 5000L)
	}

	@Test
	fun showMovies() {
		with(composeTestRule) {
			onRoot(useUnmergedTree = true).printToLog("showMovies")
			waitUntilAtLeastOneExistsCopy(hasTestTag("MovieColumn"), 1000L)

			onNodeWithText("name1", useUnmergedTree = true).assertExists()
		}
	}

	@Test
	fun showMovieDetails() {
		navigateToMovieDetails()
		with(composeTestRule) {
			onAllNodesWithText("Brazil", useUnmergedTree = true).assertAny(hasText("Brazil"))
			onNodeWithText("Movies", useUnmergedTree = true).assertDoesNotExist()

			composeTestRule.activityRule.scenario.onActivity { activity ->
				activity.onBackPressedDispatcher.onBackPressed()
			}

			onNodeWithText("Movies", useUnmergedTree = true).assertExists()
		}
	}

	@Test
	fun rateMovie_whenLoggedIn_showsRatingSlider() {
		fakeAccountRepository.isLoggedIn = true
		fakeMoviesRepository.movieDetail = MovieDetailMother.createMovieDetail().copy(personalRating = 0f)
		navigateToMovieDetails()
		with(composeTestRule) {
			onNodeWithTag("rating_slider").assertIsDisplayed()
			onNodeWithTag("rate_button").assertIsDisplayed()
		}
	}

	@Test
	fun rateMovie_whenLoggedInAndRated_showsRating() {
		fakeAccountRepository.isLoggedIn = true
		fakeMoviesRepository.movieDetail = MovieDetailMother.createMovieDetail().copy(personalRating = 0f)
		navigateToMovieDetails()
		with(composeTestRule) {
			onNodeWithTag("rating_slider").assertIsDisplayed()
			onNodeWithTag("rate_button").performClick()
			waitUntilAtLeastOneExistsCopy(hasTestTag("rating_text"), 5000L)
			onNodeWithTag("rating_text").assertIsDisplayed()
			onNodeWithTag("rating_slider").assertDoesNotExist()
		}
	}

	@Test
	fun rateMovie_whenMovieAlreadyRated_showsRating() {
		fakeAccountRepository.isLoggedIn = true
		fakeMoviesRepository.movieDetail = MovieDetailMother.createMovieDetail().copy(personalRating = 7.0f)
		navigateToMovieDetails()

		with(composeTestRule) {
			onNodeWithTag("rating_text").assertIsDisplayed()
			onNodeWithText("Your rating: 7.0").assertIsDisplayed()
			onNodeWithTag("rating_slider").assertDoesNotExist()
		}
	}

	@Test
	fun rateMovie_whenLoggedOut_ratingSectionIsNotVisible() {
		fakeAccountRepository.isLoggedIn = false
		navigateToMovieDetails()

		with(composeTestRule) {
			onNodeWithTag("rate_this_movie_title").assertDoesNotExist()
		}
	}

}
