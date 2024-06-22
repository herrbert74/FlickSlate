package com.zsoltbertalan.flickslate.data.repository

import com.github.michaelbull.result.map
import com.zsoltbertalan.flickslate.data.network.FlickSlateService
import com.zsoltbertalan.flickslate.data.network.dto.toTvDetail
import com.zsoltbertalan.flickslate.data.network.dto.toTvList
import com.zsoltbertalan.flickslate.domain.api.TvRepository
import com.zsoltbertalan.flickslate.domain.model.TvDetail
import com.zsoltbertalan.flickslate.ext.Outcome
import com.zsoltbertalan.flickslate.ext.runCatchingApi
import javax.inject.Inject

class TvAccessor @Inject constructor(
	private val flickSlateService: FlickSlateService
) : TvRepository {

	override fun getTopRatedTv(language: String) = createPager { page ->
		runCatchingApi {
			flickSlateService.getTopRatedTv(language = language, page = page)
		}.map { Pair(it.toTvList(), it.total_pages ?: 0) }
	}.flow

	override suspend fun getTvDetails(seriesId: Int): Outcome<TvDetail> {
		return com.zsoltbertalan.flickslate.ext.runCatchingApi {
			flickSlateService.getTvDetails(seriesId = seriesId).toTvDetail()
		}
	}

}
