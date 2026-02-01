package com.zsoltbertalan.flickslate.shared.ui.compose.component.paging

import io.kotest.matchers.shouldBe
import org.junit.Test

class PaginationStateTest {

	@Test
	fun `when appendPage called with same page key then items are replaced`() {
		val paginationState = PaginationState<Int, String>(
			initialPageKey = 1,
			onRequestPage = {}
		)

		val page1ItemsCache = listOf("Item 1", "Item 2")
		val page1ItemsNetwork = listOf("Item 1 Updated", "Item 2 Updated", "Item 3")

		// 1. Simulate Cache returning Page 1
		paginationState.appendPage(
			pageKey = 1,
			items = page1ItemsCache,
			nextPageKey = 2,
			isLastPage = false
		)

		paginationState.allItems shouldBe page1ItemsCache

		// 2. Simulate Network returning Page 1 (overriding cache)
		paginationState.appendPage(
			pageKey = 1,
			items = page1ItemsNetwork,
			nextPageKey = 2,
			isLastPage = false
		)

		// 3. Verify items are replaced, not appended
		paginationState.allItems shouldBe page1ItemsNetwork
	}

	@Test
	fun `when appendPage called with different page key then items are appended`() {
		val paginationState = PaginationState<Int, String>(
			initialPageKey = 1,
			onRequestPage = {}
		)

		val page1Items = listOf("Item 1", "Item 2")
		val page2Items = listOf("Item 3", "Item 4")

		// 1. Page 1
		paginationState.appendPage(
			pageKey = 1,
			items = page1Items,
			nextPageKey = 2,
			isLastPage = false
		)

		// 2. Page 2
		paginationState.appendPage(
			pageKey = 2,
			items = page2Items,
			nextPageKey = 3,
			isLastPage = false
		)

		// 3. Verify items are combined
		paginationState.allItems shouldBe (page1Items + page2Items)
	}
}
