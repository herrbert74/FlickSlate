package com.zsoltbertalan.flickslate

import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zsoltbertalan.flickslate.main.FlickSlateActivity
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetailMother
import com.zsoltbertalan.flickslate.shared.ui.compose.waitUntilAtLeastOneExistsCopy
import com.zsoltbertalan.flickslate.tv.domain.model.TvMother
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccountListRefreshTest {

	@get:Rule
	val composeTestRule = AndroidComposeTestRule<ActivityScenarioRule<FlickSlateActivity>, FlickSlateActivity>(
		activityRule = ActivityScenarioRule(
			Intent(
				ApplicationProvider.getApplicationContext(),
				FlickSlateActivity::class.java
			).apply {
				putExtra("isRunningTest", true)
			}
		),
		activityProvider = { rule ->
			var activity: FlickSlateActivity? = null
			rule.scenario.onActivity { activity = it }
			activity!!
		}
	)

	private lateinit var fakeAccountRepository: FakeAccountRepository
	private lateinit var fakeMoviesRepository: FakeMoviesRepository
	private lateinit var fakeTvRepository: FakeTvRepository

	@Before
	fun setUp() {
		composeTestRule.activityRule.scenario.onActivity { activity ->
			val testOverrides = activity.application.asTestOverrides()
			fakeAccountRepository = testOverrides.fakeAccountRepository
			fakeMoviesRepository = testOverrides.fakeMoviesRepository
			fakeTvRepository = testOverrides.fakeTvRepository
		}
		fakeAccountRepository.isLoggedIn = true
		AccountListTestState.resetToDefaults()
	}

	@Test
	fun favorites_whenUnfavoritedInMovieDetails_onBackListUpdates() {
		fakeMoviesRepository.movieDetail = MovieDetailMother
			.createMovieDetail(
				id = 1,
				title = "Brazil",
			)
			.copy(favorite = true)

		with(composeTestRule) {
			onNodeWithText("Account").performClick()
			onNodeWithText("Favorites").performClick()

			waitUntilAtLeastOneExistsCopy(hasText("Brazil"), 5000L)
			onNodeWithTag("FavoritesColumn").performScrollToNode(hasTestTag("FavoriteMoviesList"))
			onNodeWithText("Brazil", useUnmergedTree = true).assertIsDisplayed()

			onNodeWithText("Brazil", useUnmergedTree = true).performClick()
			waitUntilAtLeastOneExistsCopy(hasText("Brazil"), 5000L)

			onNodeWithTag("Movie Detail Column").performScrollToNode(hasTestTag("Favorite Button"))

			waitUntilAtLeastOneExistsCopy(hasText("Remove from favorites"), 5000L)
			onNodeWithText("Remove from favorites", ignoreCase = true).assertIsDisplayed()

			onNodeWithTag("Favorite Button").performClick()
			waitUntilAtLeastOneExistsCopy(hasText("Add to favorites"), 5000L)

			activityRule.scenario.onActivity { activity ->
				activity.onBackPressedDispatcher.onBackPressed()
			}

			onNodeWithText("Favorites").performClick()

			onNodeWithTag("FavoritesColumn").performScrollToNode(hasText("You have no favorite movies"))
			onNodeWithText("You have no favorite movies", useUnmergedTree = true).assertIsDisplayed()
			onNodeWithText("Brazil", useUnmergedTree = true).assertDoesNotExist()
		}
	}

	@Test
	fun ratings_whenMovieRatingDeletedInMovieDetails_onBackListUpdates() {
		fakeMoviesRepository.movieDetail = MovieDetailMother
			.createMovieDetail(
				id = 0,
				title = "Brazil",
			)
			.copy(personalRating = 10f)

		with(composeTestRule) {
			onNodeWithText("Account").performClick()

			waitUntilAtLeastOneExistsCopy(hasText("Brazil"), 5000L)
			onNodeWithTag("RatingsColumn").performScrollToNode(hasTestTag("RatedMoviesList"))
			onNodeWithText("Brazil", useUnmergedTree = true).assertIsDisplayed()

			onNodeWithText("Brazil", useUnmergedTree = true).performClick()
			waitUntilAtLeastOneExistsCopy(hasText("Brazil"), 5000L)

			onNodeWithTag("Movie Detail Column").performScrollToNode(hasTestTag("Delete Rating Button"))
			waitUntilAtLeastOneExistsCopy(hasTestTag("Delete Rating Button"), 5000L)
			onNodeWithTag("Delete Rating Button").performClick()

			activityRule.scenario.onActivity { activity ->
				activity.onBackPressedDispatcher.onBackPressed()
			}

			waitUntilAtLeastOneExistsCopy(hasText("You have no rated movies"), 5000L)
			onNodeWithTag("RatingsColumn").performScrollToNode(hasText("You have no rated movies"))
			onNodeWithText("You have no rated movies", useUnmergedTree = true).assertIsDisplayed()
			onNodeWithText("Brazil", useUnmergedTree = true).assertDoesNotExist()
		}
	}

	@Test
	fun favorites_whenUnfavoritedInTvDetails_onBackListUpdates() {
		fakeTvRepository.tvDetail = TvMother.createTvDetailWithImages(id = 2).copy(favorite = true)

		with(composeTestRule) {
			onNodeWithText("Account").performClick()
			onNodeWithText("Favorites").performClick()

			onNodeWithTag("FavoritesColumn").performScrollToNode(hasTestTag("FavoriteTvShowsList"))
			val detectoristsCardMatcher = hasClickAction().and(hasAnyDescendant(hasText("Detectorists")))
			waitUntilAtLeastOneExistsCopy(detectoristsCardMatcher, 5000L)
			onAllNodes(detectoristsCardMatcher, useUnmergedTree = true)[0].performClick()
			waitUntilAtLeastOneExistsCopy(hasText("Detectorists"), 5000L)

			onNodeWithTag("Tv Detail Column").performScrollToNode(hasTestTag("Favorite Button"))
			onNodeWithText("Remove from favorites", ignoreCase = true).assertIsDisplayed()

			onNodeWithTag("Favorite Button").performClick()
			waitUntilAtLeastOneExistsCopy(hasText("Add to favorites"), 5000L)
			onNodeWithText("Add to favorites", ignoreCase = true).assertIsDisplayed()

			activityRule.scenario.onActivity { activity ->
				activity.onBackPressedDispatcher.onBackPressed()
			}

			onNodeWithText("Favorites").performClick()
			onNodeWithTag("FavoritesColumn").performScrollToNode(hasText("You have no favorite TV shows"))
			onNodeWithText("You have no favorite TV shows", useUnmergedTree = true).assertIsDisplayed()
			onNodeWithText("Detectorists", useUnmergedTree = true).assertDoesNotExist()
		}
	}

	@Test
	fun ratings_whenTvShowRatingDeletedInTvDetails_onBackListUpdates() {
		fakeTvRepository.tvDetail = TvMother.createTvDetailWithImages(id = 0).copy(personalRating = 7f)

		with(composeTestRule) {
			onNodeWithText("Account").performClick()

			onNodeWithTag("RatingsColumn").performScrollToNode(hasTestTag("RatedTvShowsList"))
			val detectoristsCardMatcher = hasClickAction().and(hasAnyDescendant(hasText("Detectorists")))
			waitUntilAtLeastOneExistsCopy(detectoristsCardMatcher, 5000L)
			onAllNodes(detectoristsCardMatcher, useUnmergedTree = true)[0].performClick()
			waitUntilAtLeastOneExistsCopy(hasText("Detectorists"), 5000L)

			onNodeWithTag("Tv Detail Column").performScrollToNode(hasTestTag("Delete Rating Button"))
			waitUntilAtLeastOneExistsCopy(hasTestTag("Delete Rating Button"), 5000L)
			onNodeWithTag("Delete Rating Button").performClick()

			activityRule.scenario.onActivity { activity ->
				activity.onBackPressedDispatcher.onBackPressed()
			}

			onNodeWithText("Ratings").performClick()

			onNodeWithTag("RatingsColumn").performScrollToNode(hasText("You have no rated TV shows"))
			onNodeWithText("You have no rated TV shows", useUnmergedTree = true).assertIsDisplayed()
			onNodeWithText("Detectorists", useUnmergedTree = true).assertDoesNotExist()
		}
	}
}
