package com.zsoltbertalan.flickslate.search.ui.main

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.requestFocus
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zsoltbertalan.flickslate.shared.ui.compose.waitUntilAtLeastOneExistsCopy
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchScreenTest {

	@get:Rule
	val composeTestRule = createAndroidComposeRule<ComponentActivity>()

	@get:Rule
	val mainDispatcherRule = MainDispatcherRule()

	@Test
	fun search() {
		lateinit var searchViewModel: SearchViewModel
		with(composeTestRule) {
			val metroVmf = TestSearchMetroViewModelFactory()
			setContent {
				CompositionLocalProvider(LocalMetroViewModelFactory provides metroVmf) {
					searchViewModel = metroViewModel()
					val searchState by searchViewModel.searchStateData.collectAsStateWithLifecycle()
					SearchScreen(
						searchState = searchState,
						{
							CoroutineScope(mainDispatcherRule.dispatcher).launch {
								searchViewModel.emitEvent(it)
							}
						},
						{ _, _ -> },
						{},
						{}
					)
				}
			}

			val searchBar = onNode(hasContentDescription("Searchbar"))
			searchBar.assertIsDisplayed()

			searchBar.requestFocus()

			searchBar.performTextInput("AAA")

			searchBar.assertTextEquals("AAA")
			CoroutineScope(mainDispatcherRule.dispatcher).launch {
				searchViewModel.emitEvent(SearchEvent.SearchQuery("AAA"))
			}
			(mainDispatcherRule.dispatcher as TestDispatcher).scheduler.advanceUntilIdle()

		}
		composeTestRule.waitUntilAtLeastOneExistsCopy(hasTestTag("SearchResultColumn"), timeoutMillis = 5_000L)
		composeTestRule.onNodeWithText("name1", useUnmergedTree = true).assertExists()

	}

}
