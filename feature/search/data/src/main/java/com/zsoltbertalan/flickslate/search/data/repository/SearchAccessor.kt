package com.zsoltbertalan.flickslate.search.data.repository

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.search.data.network.SearchMoviesRemoteDataSource
import com.zsoltbertalan.flickslate.search.domain.api.SearchRepository
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import javax.inject.Inject

@ContributesBinding(ActivityRetainedScope::class)
@SingleIn(ActivityRetainedScope::class)
internal class SearchAccessor @Inject constructor(
	private val searchMoviesRemoteDataSource: SearchMoviesRemoteDataSource
) : SearchRepository {

	override suspend fun getSearchResult(query: String, page: Int): Outcome<PagingReply<Movie>> =
		searchMoviesRemoteDataSource.searchMovies(query = query, page = page)

}
