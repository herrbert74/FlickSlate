package com.zsoltbertalan.flickslate.search.data.network

import com.zsoltbertalan.flickslate.search.data.api.SearchMoviesDataSource
import com.zsoltbertalan.flickslate.shared.data.network.model.MoviesReplyDto
import com.zsoltbertalan.flickslate.shared.data.network.model.toPagingReply
import com.zsoltbertalan.flickslate.shared.data.util.safeCall
import com.zsoltbertalan.flickslate.shared.model.Movie
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
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
