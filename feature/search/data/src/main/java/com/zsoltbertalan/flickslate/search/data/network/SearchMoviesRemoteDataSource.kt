package com.zsoltbertalan.flickslate.search.data.network

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.search.data.api.SearchMoviesDataSource
import com.zsoltbertalan.flickslate.shared.data.network.model.MoviesReplyDto
import com.zsoltbertalan.flickslate.shared.data.network.model.toPagingReply
import com.zsoltbertalan.flickslate.shared.data.util.safeCall
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
class SearchMoviesRemoteDataSource @Inject constructor(
	private val searchService: SearchService
) : SearchMoviesDataSource.Remote {

	override suspend fun searchMovies(query: String, page: Int?): Outcome<PagingReply<Movie>> {
		return safeCall(
			{
				searchService.searchMovies(
					query = query,
					language = "en",
					page = page
				)
			},
			MoviesReplyDto::toPagingReply
		)
	}

}
