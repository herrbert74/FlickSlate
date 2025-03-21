package com.zsoltbertalan.flickslate.search.data.api

import com.zsoltbertalan.flickslate.shared.model.Movie
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome

interface SearchMoviesDataSource {

	interface Remote {

		suspend fun searchMovies(query: String, page: Int?): Outcome<PagingReply<Movie>>

	}

}
