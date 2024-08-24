package com.zsoltbertalan.flickslate.domain.api

import com.zsoltbertalan.flickslate.common.util.Outcome
import com.zsoltbertalan.flickslate.domain.model.TvShow
import com.zsoltbertalan.flickslate.domain.model.TvDetail
import com.zsoltbertalan.flickslate.domain.model.PagingReply
import kotlinx.coroutines.flow.Flow

interface TvRepository {
	fun getTopRatedTv(
		page: Int,
	): Flow<Outcome<PagingReply<TvShow>>>

	suspend fun getTvDetails(seriesId: Int): Outcome<TvDetail>

}
