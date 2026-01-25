package com.zsoltbertalan.flickslate

import android.content.Intent
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performSemanticsAction
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
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
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class TvSeasonDetailTest {

	@get:Rule(order = 0)
	val hiltRule = HiltAndroidRule(this)

	@get:Rule(order = 1)
	val composeRule = AndroidComposeTestRule<ActivityScenarioRule<FlickSlateActivity>, FlickSlateActivity>(
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
	val fakeTvRatingsRepository: TvRatingsRepository = FakeTvRatingsRepository()

	@Inject
	lateinit var fakeAccountRepository: FakeAccountRepository

	@Inject
	lateinit var fakeTvRepository: FakeTvRepository

	@Before
	fun setup() {
		hiltRule.inject()
		fakeAccountRepository.isLoggedIn = true
		fakeTvRepository.tvDetail = TvMother.createTvDetailWithImages().copy(personalRating = 8f)
	}

	private fun navigateToSeasonDetail() {
		with(composeRule) {
			onNodeWithText("Tv").performClick()
			waitUntilAtLeastOneExistsCopy(hasTestTag("MovieColumn"), 1000L)
			onNodeWithText("Detectorists", ignoreCase = true).performClick()
			waitUntilAtLeastOneExistsCopy(hasText("Detectorists"), 5000L)
			onNodeWithTag("Tv Detail Column").performScrollToNode(hasTestTag("Seasons Row"))
			onNodeWithTag("Seasons Row").assertExists()
			onNodeWithTag("Season Chip 1").performClick()
			waitUntilAtLeastOneExistsCopy(hasTestTag("Tv Season Detail Column"), 5000L)
		}
	}

	private fun expandFirstEpisode() {
		with(composeRule) {
			onNodeWithTag("Tv Season Detail Column")
				.performScrollToNode(hasText("1. Episode 1", substring = false, ignoreCase = true))
			onNodeWithText("1. Episode 1", useUnmergedTree = true).performClick()
		}
	}

	private fun scrollToRateButton() {
		composeRule
			.onNodeWithTag("Tv Season Detail Column")
			.performScrollToNode(hasTestTag("Rate Button"))
	}

	@Test
	fun rateEpisode_whenNotRated_showsRateButtonOnly() {
		fakeTvRepository.tvEpisodeDetail = TvMother.createTvEpisodeDetail().copy(personalRating = -1f)
		navigateToSeasonDetail()
		expandFirstEpisode()
		scrollToRateButton()

		with(composeRule) {
			onNodeWithTag("Rate Episode 1", useUnmergedTree = true).assertIsDisplayed()
			onNodeWithTag("Rate Button").assertIsDisplayed()
			onNodeWithText("Change rating", ignoreCase = true).assertIsNotDisplayed()
		}
		fakeTvRepository.tvEpisodeDetail = TvMother.createTvEpisodeDetail()
	}

	@Test
	fun changeEpisodeRating_whenAlreadyRated_updatesValue() {
		// fakeTvRepository.tvDetail = TvMother.createTvDetailWithImages().copy(personalRating = 8f)
		navigateToSeasonDetail()
		expandFirstEpisode()
		scrollToRateButton()

		with(composeRule) {
			onNodeWithText("Change rating", useUnmergedTree = true).assertIsDisplayed()
			onNodeWithText("Your rating: 6.0").assertIsDisplayed()
			onNodeWithTag("Rating Slider").performSemanticsAction(SemanticsActions.SetProgress) { it(9.0f) }
			onNodeWithTag("Rate Button").performClick()
			onNodeWithText("Your rating: 9.0").assertIsDisplayed()
		}
	}

	@Test
	fun deleteEpisodeRating_whenAlreadyRated_removesRating() {
		fakeTvRepository.tvEpisodeDetail = TvMother.createTvEpisodeDetail().copy(personalRating = 6f)
		navigateToSeasonDetail()
		expandFirstEpisode()
		scrollToRateButton()

		with(composeRule) {
			waitUntilAtLeastOneExistsCopy(hasText("Delete rating", ignoreCase = true), 5000L)
			onNodeWithTag("Delete Rating Button", useUnmergedTree = true).performClick()
			onNodeWithText("Your rating: 8.0").assertDoesNotExist()
		}
	}
}
