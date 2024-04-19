package com.zsoltbertalan.flickslate.data.repository

import com.github.michaelbull.result.map
import com.zsoltbertalan.flickslate.data.network.FlickSlateService
import com.zsoltbertalan.flickslate.data.network.dto.toTvDetail
import com.zsoltbertalan.flickslate.data.network.dto.toTvList
import com.zsoltbertalan.flickslate.domain.api.TvRepository
import com.zsoltbertalan.flickslate.domain.model.TvDetail
import com.zsoltbertalan.flickslate.ext.ApiResult
import com.zsoltbertalan.flickslate.ext.apiRunCatching
import javax.inject.Inject

class TvAccessor @Inject constructor(
	private val flickSlateService: FlickSlateService
) : TvRepository {

	override fun getTopRatedTv(
		language: String,
	) = createPager { page ->
		apiRunCatching {
			flickSlateService.getTopRatedTv(language = language, page = page)
		}.map { Pair(it.toTvList(), it.total_pages ?: 0) }
	}.flow

	override suspend fun getTvDetails(series_id: Int): ApiResult<TvDetail> {
		return apiRunCatching {
			flickSlateService.getTvDetails(series_id = series_id).toTvDetail()
		}
	}
}