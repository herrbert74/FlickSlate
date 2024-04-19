package com.zsoltbertalan.flickslate.data.repository

import com.zsoltbertalan.flickslate.data.network.FlickSlateService
import com.zsoltbertalan.flickslate.data.network.dto.toMoviesResponse
import com.zsoltbertalan.flickslate.domain.api.SearchRepository
import com.zsoltbertalan.flickslate.ext.ApiResult
import com.zsoltbertalan.flickslate.ext.apiRunCatching
import javax.inject.Inject

class SearchAccessor @Inject constructor(private val flickSlateService: FlickSlateService) : SearchRepository {

	override suspend fun getSearchResult(query: String, page: Int): ApiResult<List<String>> =
		apiRunCatching {
			flickSlateService.searchMovies(
				query = query,
				language = "en",
				page = page
			).toMoviesResponse().movies.map { it.title }
		}

}
