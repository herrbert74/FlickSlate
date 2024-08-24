package com.zsoltbertalan.flickslate.data.repository

import com.zsoltbertalan.flickslate.data.network.FlickSlateService
import com.zsoltbertalan.flickslate.data.network.dto.toMoviesResponse
import com.zsoltbertalan.flickslate.domain.api.SearchRepository
import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.common.util.Outcome
import com.zsoltbertalan.flickslate.common.util.runCatchingApi
import javax.inject.Inject

class SearchAccessor @Inject constructor(private val flickSlateService: FlickSlateService) : SearchRepository {

	override suspend fun getSearchResult(query: String, page: Int): Outcome<List<Movie>> =
		runCatchingApi {
			flickSlateService.searchMovies(
				query = query,
				language = "en",
				page = page
			).toMoviesResponse().pagingList
		}

}
