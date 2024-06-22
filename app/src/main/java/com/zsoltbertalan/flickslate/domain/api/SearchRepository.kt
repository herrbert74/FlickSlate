package com.zsoltbertalan.flickslate.domain.api

import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.ext.Outcome

interface SearchRepository {

	suspend fun getSearchResult(query: String, page: Int = 1): Outcome<List<Movie>>

}