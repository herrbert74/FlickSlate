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
import com.zsoltbertalan.flickslate.shared.ui.compose.waitUntilAtLeastOneExistsCopy
import com.zsoltbertalan.flickslate.tv.data.repository.AutoBindTvRatingsAccessorActivityRetainedModule
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
@UninstallModules(AutoBindTvRatingsAccessorActivityRetainedModule::class)
@RunWith(AndroidJUnit4::class)
class TvDetailsTest {

	@get:Rule(order = 0)
	val hiltAndroidRule = HiltAndroidRule(this)

	@get:Rule(order = 1)
	val composeTestRule = createAndroidComposeRule<FlickSlateActivity>()

	@BindValue
	val fakeTvRatingsRepository: TvRatingsRepository = FakeTvRatingsRepository()

	@Inject
	lateinit var fakeAccountRepository: FakeAccountRepository

	@Inject
	lateinit var fakeTvRepository: FakeTvRepository

	@Before
	fun setUp() {
		hiltAndroidRule.inject()
	}

	private fun navigateToTvDetails() {
		composeTestRule.onNodeWithText("Tv").performClick()

		composeTestRule.waitUntilAtLeastOneExistsCopy(hasTestTag("MovieColumn"), 1000L)
		composeTestRule.onNodeWithText("Detectorists", ignoreCase = true).performClick()
		composeTestRule.waitUntilAtLeastOneExistsCopy(hasText("Detectorists"), 5000L)
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
		navigateToTvDetails()
		with(composeTestRule) {
			onAllNodesWithText("Detectorists", useUnmergedTree = true).assertAny(hasText("Detectorists"))
			onNodeWithText("Movies", useUnmergedTree = true).assertDoesNotExist()
			onNodeWithText("Season 1", useUnmergedTree = true).assertExists()

			composeTestRule.activityRule.scenario.onActivity { activity ->
				activity.onBackPressedDispatcher.onBackPressed()
			}

			onNodeWithText("Movies", useUnmergedTree = true).assertExists()
		}
	}

	@Test
	fun rateTvShow_whenLoggedIn_showsRatingSlider() {
		fakeAccountRepository.isLoggedIn = true
		fakeTvRepository.tvDetail = TvMother.createTvDetailWithImages()

		navigateToTvDetails()

		with(composeTestRule) {
			onNodeWithTag("Tv Detail Column").performScrollToNode(hasTestTag("Rate Button"))

			onNodeWithTag("Rating Slider").assertIsDisplayed()
			onNodeWithTag("Rate Button").assertIsDisplayed()
			onNodeWithTag("Delete Rating Button").assertIsNotDisplayed()
			onNodeWithText("Change rating", ignoreCase = true).assertDoesNotExist()
		}
	}

	@Test
	fun rateTvShow_whenLoggedInAndRated_showsRating() {
		fakeAccountRepository.isLoggedIn = true
		fakeTvRepository.tvDetail = TvMother.createTvDetailWithImages()

		navigateToTvDetails()

		with(composeTestRule) {
			onNodeWithTag("Tv Detail Column").performScrollToNode(hasTestTag("Rate Button"))
			onNodeWithTag("Rate Button").performClick()
			onNodeWithTag("Tv Detail Column").performScrollToNode(hasText("Image gallery"))

			waitUntilAtLeastOneExistsCopy(hasTestTag("Rating Text"), 5000L)
			onNodeWithTag("Rating Text").assertIsDisplayed()
			onNodeWithTag("Delete Rating Button").assertIsDisplayed()
			onNodeWithText("Change rating", ignoreCase = true).assertIsDisplayed()
		}
	}

	@Test
	fun rateTvShow_whenAlreadyRated_showsRating() {
		fakeAccountRepository.isLoggedIn = true
		fakeTvRepository.tvDetail = TvMother.createTvDetailWithImages(personalRating = 8f)

		navigateToTvDetails()

		with(composeTestRule) {
			onNodeWithTag("Tv Detail Column").performScrollToNode(hasTestTag("Rating Text"))
			onNodeWithTag("Rating Text").assertIsDisplayed()
			onNodeWithText("Your rating: 8.0").assertIsDisplayed()
			onNodeWithTag("Rating Slider").assertIsDisplayed()
			onNodeWithTag("Delete Rating Button").assertIsDisplayed()
			onNodeWithText("Change rating", ignoreCase = true).assertIsDisplayed()
		}
	}

	@Test
	fun rateTvShow_whenLoggedOut_ratingSectionHidden() {
		fakeAccountRepository.isLoggedIn = false
		navigateToTvDetails()

		with(composeTestRule) {
			onNodeWithTag("Rate this show title").assertDoesNotExist()
		}
	}

	@Test
	fun changeTvShowRating_whenAlreadyRated_updatesRating() {
		fakeAccountRepository.isLoggedIn = true
		fakeTvRepository.tvDetail = TvMother.createTvDetailWithImages(personalRating = 7f)

		navigateToTvDetails()

		with(composeTestRule) {
			onNodeWithTag("Tv Detail Column").performScrollToNode(hasTestTag("Rating Slider"))
			onNodeWithTag("Rating Slider").performSemanticsAction(SemanticsActions.SetProgress) { it(9.0f) }
			onNodeWithTag("Rate Button").performClick()
			onNodeWithText("Your rating: 9.0").assertIsDisplayed()
		}
	}

	@Test
	fun deleteTvShowRating_whenAlreadyRated_removesRating() {
		fakeAccountRepository.isLoggedIn = true
		fakeTvRepository.tvDetail = TvMother.createTvDetailWithImages(personalRating = 7f)

		navigateToTvDetails()

		with(composeTestRule) {
			onNodeWithTag("Tv Detail Column").performScrollToNode(hasTestTag("Delete Rating Button"))
			onNodeWithTag("Delete Rating Button").performClick()
			onNodeWithText("Your rating: 7.0").assertDoesNotExist()
		}
	}

}
