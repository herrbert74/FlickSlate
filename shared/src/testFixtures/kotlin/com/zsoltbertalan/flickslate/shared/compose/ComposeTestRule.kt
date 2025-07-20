package com.zsoltbertalan.flickslate.shared.compose

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule

/**
 * Copied from [androidx.compose.ui.test.ComposeUiTest] to override useUnmergedTree.
 * The override is only available for onXXX matchers.
 */
inline fun <reified A : ComponentActivity>
	AndroidComposeTestRule<ActivityScenarioRule<A>, A>.waitUntilAtLeastOneExistsCopy(
	matcher: SemanticsMatcher,
	timeoutMillis: Long = 1_000L
) {
	waitUntil(timeoutMillis) {
		onAllNodes(matcher, true).fetchSemanticsNodes().isNotEmpty()
	}
}
