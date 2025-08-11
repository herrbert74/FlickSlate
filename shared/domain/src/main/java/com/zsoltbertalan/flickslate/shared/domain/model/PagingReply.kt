package com.zsoltbertalan.flickslate.shared.domain.model

/**
 * Class wrapping one page from a paging response
 * @param pagingList is the list
 * @param isLastPage is metadata used in the Presentation layer
 * @param pageData used to save paging metadata into the database
 */
data class PagingReply<V>(
	val pagingList: List<V>,
	val isLastPage: Boolean,
	val pageData: PageData,
)
