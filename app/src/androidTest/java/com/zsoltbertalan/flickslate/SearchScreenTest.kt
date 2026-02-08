package com.zsoltbertalan.flickslate

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
import com.zsoltbertalan.flickslate.search.ui.main.SearchScreen
import com.zsoltbertalan.flickslate.search.ui.main.SearchViewModel
import com.zsoltbertalan.flickslate.shared.ui.compose.waitUntilAtLeastOneExistsCopy
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchScreenTest {

	@get:Rule
	val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

	@Test
	fun search() {
		with(composeTestRule) {
			val metroVmf = (activity.application as FlickSlateApp).appGraph.metroViewModelFactory
			setContent {
				CompositionLocalProvider(LocalMetroViewModelFactory provides metroVmf) {
					val searchViewModel: SearchViewModel = metroViewModel()
					val searchState by searchViewModel.searchStateData.collectAsStateWithLifecycle()
					SearchScreen(
						searchState = searchState,
						{
							CoroutineScope(Dispatchers.Default).launch {
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

		}
		composeTestRule.waitUntilAtLeastOneExistsCopy(hasTestTag("SearchResultColumn"))
		composeTestRule.onNodeWithText("name1", useUnmergedTree = true).assertExists()

	}

}
