package com.zsoltbertalan.flickslate.domain.api

import com.zsoltbertalan.flickslate.ext.ApiResult

interface SearchRepository {

	suspend fun getSearchResult(query: String, page: Int = 1): ApiResult<List<String>>

}