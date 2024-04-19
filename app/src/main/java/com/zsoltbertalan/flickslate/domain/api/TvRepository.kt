package com.zsoltbertalan.flickslate.domain.api

import androidx.paging.PagingData
import com.zsoltbertalan.flickslate.domain.model.Tv
import com.zsoltbertalan.flickslate.domain.model.TvDetail
import com.zsoltbertalan.flickslate.ext.ApiResult
import kotlinx.coroutines.flow.Flow

interface TvRepository {
	fun getTopRatedTv(
		language: String,
	): Flow<PagingData<Tv>>

	suspend fun getTvDetails(series_id: Int): ApiResult<TvDetail>
}