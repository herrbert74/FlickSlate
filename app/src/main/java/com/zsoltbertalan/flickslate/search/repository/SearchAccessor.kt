package com.zsoltbertalan.flickslate.search.repository

import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.search.data.network.SearchMoviesRemoteDataSource
import com.zsoltbertalan.flickslate.search.domain.api.SearchRepository
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import javax.inject.Inject

class SearchAccessor @Inject constructor(
	private val searchMoviesRemoteDataSource: SearchMoviesRemoteDataSource
) : SearchRepository {

	override suspend fun getSearchResult(query: String, page: Int): Outcome<PagingReply<Movie>> =

		searchMoviesRemoteDataSource.searchMovies(query = query, page = page)

}
