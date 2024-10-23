package com.zsoltbertalan.flickslate.shared.domain.model

class PagingReply<V>(
	val pagingList: List<V>,
	val isLastPage: Boolean,
	val pageData: PageData,
)
