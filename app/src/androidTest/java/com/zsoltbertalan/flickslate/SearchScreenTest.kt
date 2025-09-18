package com.zsoltbertalan.flickslate

import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.requestFocus
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zsoltbertalan.flickslate.search.ui.main.SearchScreen
import com.zsoltbertalan.flickslate.search.ui.main.SearchViewModel
import com.zsoltbertalan.flickslate.shared.ui.compose.waitUntilAtLeastOneExistsCopy
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SearchScreenTest {

	@get:Rule(order = 0)
	val hiltAndroidRule = HiltAndroidRule(this)

	@get:Rule(order = 1)
	val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

	@Before
	fun setUp() {
		hiltAndroidRule.inject()
	}

	@Test
	fun search() {
		with(composeTestRule) {
			setContent {
				val searchViewModel: SearchViewModel = hiltViewModel()
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
