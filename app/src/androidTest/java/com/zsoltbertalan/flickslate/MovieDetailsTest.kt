package com.zsoltbertalan.flickslate

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performSemanticsAction
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
		fakeMoviesRepository.movieDetail = MovieDetailMother.createMovieDetail()
		navigateToMovieDetails()
		with(composeTestRule) {
			onNodeWithTag("Movie Detail Column").performScrollToNode(hasTestTag("Rate Button"))
			onNodeWithTag("Rate Button").assertIsDisplayed()
			onNodeWithTag("Delete Rating Button").assertIsNotDisplayed()
			onNodeWithText("Update Rating").assertIsNotDisplayed()
		}
	}

	@Test
	fun rateMovie_whenLoggedInAndRated_showsRating() {
		fakeAccountRepository.isLoggedIn = true
		fakeMoviesRepository.movieDetail = MovieDetailMother.createMovieDetail()
		navigateToMovieDetails()
		with(composeTestRule) {
			onNodeWithTag("Movie Detail Column").performScrollToNode(hasTestTag("Rate Button"))
			onNodeWithTag("Rate Button").performClick()
			onNodeWithTag("Movie Detail Column").performScrollToNode(hasText("Image gallery"))
			waitUntilAtLeastOneExistsCopy(hasTestTag("Rating Text"), 5000L)
			onNodeWithTag("Rating Text").assertIsDisplayed()
			onNodeWithTag("Delete Rating Button").assertIsDisplayed()
			onNodeWithText("Update Rating", ignoreCase = true).assertIsDisplayed()
		}
	}

	@Test
	fun rateMovie_whenMovieAlreadyRated_showsRating() {
		fakeAccountRepository.isLoggedIn = true
		fakeMoviesRepository.movieDetail = MovieDetailMother.createMovieDetail().copy(personalRating = 7.0f)
		navigateToMovieDetails()

		with(composeTestRule) {
			onNodeWithTag("Movie Detail Column").performScrollToNode(hasTestTag("Rating Text"))
			onNodeWithTag("Rating Text").assertIsDisplayed()
			onNodeWithText("Your rating: 7.0").assertIsDisplayed()
			waitUntilAtLeastOneExistsCopy(hasTestTag("Delete Rating Button"), 5000L)
			onNodeWithTag("Delete Rating Button").assertIsDisplayed()
			onNodeWithText("Update Rating", ignoreCase = true).assertIsDisplayed()
		}
	}

	@Test
	fun rateMovie_whenLoggedOut_ratingSectionIsNotVisible() {
		fakeAccountRepository.isLoggedIn = false
		navigateToMovieDetails()

		with(composeTestRule) {
			onNodeWithTag("Rate this movie title").assertDoesNotExist()
		}
	}

	@Test
	fun changeMovieRating_whenAlreadyRated_updatesRating() {
		fakeAccountRepository.isLoggedIn = true
		fakeMoviesRepository.movieDetail = MovieDetailMother.createMovieDetail().copy(personalRating = 7.0f)
		navigateToMovieDetails()

		with(composeTestRule) {
			onNodeWithTag("Movie Detail Column").performScrollToNode(hasTestTag("Rating Text"))
			onNodeWithTag("Rating Text").assertIsDisplayed()
			onNodeWithTag("Rating Slider").performSemanticsAction(SemanticsActions.SetProgress) { it(9.0f) }
			onNodeWithTag("Rate Button").performClick()
			onNodeWithText("Your rating: 9.0").assertIsDisplayed()
		}
	}

	@Test
	fun deleteMovieRating_whenAlreadyRated_removesRating() {
		fakeAccountRepository.isLoggedIn = true
		fakeMoviesRepository.movieDetail = MovieDetailMother.createMovieDetail().copy(personalRating = 7.0f)
		navigateToMovieDetails()

		with(composeTestRule) {
			onNodeWithTag("Movie Detail Column").performScrollToNode(hasTestTag("Delete Rating Button"))
			onNodeWithTag("Delete Rating Button").assertIsDisplayed()
			onNodeWithTag("Delete Rating Button").performClick()
			onNodeWithText("Your rating: 7.0").assertDoesNotExist()
		}
	}

}
