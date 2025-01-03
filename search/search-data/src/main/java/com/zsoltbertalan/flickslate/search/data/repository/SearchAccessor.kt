package com.zsoltbertalan.flickslate.search.data.repository

import com.zsoltbertalan.flickslate.search.data.network.SearchMoviesRemoteDataSource
import com.zsoltbertalan.flickslate.search.domain.api.SearchRepository
import com.zsoltbertalan.flickslate.shared.model.Movie
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import dagger.hilt.android.scopes.ViewModelScoped
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ViewModelScoped
class SearchAccessor @Inject constructor(
	private val searchMoviesRemoteDataSource: SearchMoviesRemoteDataSource
) : SearchRepository {

	override suspend fun getSearchResult(query: String, page: Int): Outcome<PagingReply<Movie>> =
		searchMoviesRemoteDataSource.searchMovies(query = query, page = page)

}
