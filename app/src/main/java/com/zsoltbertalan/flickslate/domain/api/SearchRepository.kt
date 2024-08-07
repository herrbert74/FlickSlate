package com.zsoltbertalan.flickslate.domain.api

import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.common.util.Outcome

interface SearchRepository {

	suspend fun getSearchResult(query: String, page: Int = 1): Outcome<List<Movie>>

}
