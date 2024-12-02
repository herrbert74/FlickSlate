package com.zsoltbertalan.flickslate.search.data.network

import com.zsoltbertalan.flickslate.movies.data.network.model.MoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.toMoviesReply
import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.search.data.api.SearchMoviesDataSource
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.shared.data.util.safeCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchMoviesRemoteDataSource @Inject constructor(
	private val searchService: SearchService
) : SearchMoviesDataSource.Remote {

	override suspend fun searchMovies(query: String, page: Int?): Outcome<PagingReply<Movie>> {
		return com.zsoltbertalan.flickslate.shared.data.util.safeCall(
			{
				searchService.searchMovies(
					query = query,
					language = "en",
					page = page
				)
			},
			MoviesReplyDto::toMoviesReply
		)
	}

}

