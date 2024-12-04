package com.zsoltbertalan.flickslate.search.data.repository

import com.zsoltbertalan.flickslate.shared.model.Movie
import com.zsoltbertalan.flickslate.search.data.network.SearchMoviesRemoteDataSource
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import javax.inject.Inject

class SearchAccessor @Inject constructor(
	private val searchMoviesRemoteDataSource: SearchMoviesRemoteDataSource
) : com.zsoltbertalan.flickslate.search.domain.api.SearchRepository {

	override suspend fun getSearchResult(query: String, page: Int): Outcome<PagingReply<Movie>> =

		searchMoviesRemoteDataSource.searchMovies(query = query, page = page)

}
