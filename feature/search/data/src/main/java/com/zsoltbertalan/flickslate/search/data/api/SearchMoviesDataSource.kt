package com.zsoltbertalan.flickslate.search.data.api

import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome

internal interface SearchMoviesDataSource {

	interface Remote {

		suspend fun searchMovies(query: String, page: Int?): Outcome<PagingReply<Movie>>

	}

}
