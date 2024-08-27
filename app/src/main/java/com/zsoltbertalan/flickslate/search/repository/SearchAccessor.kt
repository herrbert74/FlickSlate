package com.zsoltbertalan.flickslate.search.repository

import com.zsoltbertalan.flickslate.movies.data.network.model.toMoviesReply
import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.search.domain.api.SearchRepository
import com.zsoltbertalan.flickslate.search.data.network.SearchService
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.shared.util.runCatchingApi
import javax.inject.Inject

class SearchAccessor @Inject constructor(private val searchService: SearchService) : SearchRepository {

	override suspend fun getSearchResult(query: String, page: Int): Outcome<List<Movie>> =
		runCatchingApi {
			searchService.searchMovies(
				query = query,
				language = "en",
				page = page
			).toMoviesReply().pagingList
		}

}
