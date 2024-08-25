package com.zsoltbertalan.flickslate.search.domain.api

import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.util.Outcome

interface SearchRepository {

	suspend fun getSearchResult(query: String, page: Int = 1): Outcome<List<Movie>>

}
