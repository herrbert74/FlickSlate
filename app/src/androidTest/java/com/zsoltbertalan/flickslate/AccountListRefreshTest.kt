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
import com.zsoltbertalan.flickslate.movies.data.repository.AutoBindMovieFavoritesAccessorActivityRetainedModule
import com.zsoltbertalan.flickslate.movies.data.repository.AutoBindMovieRatingsAccessorActivityRetainedModule
import com.zsoltbertalan.flickslate.movies.domain.api.MovieFavoritesRepository
import com.zsoltbertalan.flickslate.movies.domain.api.MovieRatingsRepository
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetailMother
import com.zsoltbertalan.flickslate.shared.ui.compose.waitUntilAtLeastOneExistsCopy
import com.zsoltbertalan.flickslate.tv.data.repository.AutoBindTvFavoritesAccessorActivityRetainedModule
import com.zsoltbertalan.flickslate.tv.data.repository.AutoBindTvRatingsAccessorActivityRetainedModule
import com.zsoltbertalan.flickslate.tv.domain.api.TvFavoritesRepository
import com.zsoltbertalan.flickslate.tv.domain.api.TvRatingsRepository
import com.zsoltbertalan.flickslate.tv.domain.model.TvMother
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
@UninstallModules(
	AutoBindMovieRatingsAccessorActivityRetainedModule::class,
	AutoBindMovieFavoritesAccessorActivityRetainedModule::class,
	AutoBindTvRatingsAccessorActivityRetainedModule::class,
	AutoBindTvFavoritesAccessorActivityRetainedModule::class,
)
@RunWith(AndroidJUnit4::class)
class AccountListRefreshTest {

	@get:Rule(order = 0)
	val hiltAndroidRule = HiltAndroidRule(this)

	@get:Rule(order = 1)
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

	@BindValue
	val fakeMovieRatingsRepository: MovieRatingsRepository = FakeMovieRatingsRepository()

	@BindValue
	val fakeMovieFavoritesRepository: MovieFavoritesRepository = FakeMovieFavoritesRepository()

	@BindValue
	val fakeTvRatingsRepository: TvRatingsRepository = FakeTvRatingsRepository()

	@BindValue
	val fakeTvFavoritesRepository: TvFavoritesRepository = FakeTvFavoritesRepository()

	@Inject
	lateinit var fakeAccountRepository: FakeAccountRepository

	@Inject
	lateinit var fakeMoviesRepository: FakeMoviesRepository

	@Inject
	lateinit var fakeTvRepository: FakeTvRepository

	@Before
	fun setUp() {
		hiltAndroidRule.inject()
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
