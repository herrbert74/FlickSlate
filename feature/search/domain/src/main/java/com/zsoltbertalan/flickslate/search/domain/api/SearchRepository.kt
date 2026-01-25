package com.zsoltbertalan.flickslate.search.domain.api

import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome

interface SearchRepository {

	suspend fun getSearchResult(query: String, page: Int = 1): Outcome<PagingReply<Movie>>

}
