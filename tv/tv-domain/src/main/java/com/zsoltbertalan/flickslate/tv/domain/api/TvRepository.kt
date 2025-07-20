package com.zsoltbertalan.flickslate.tv.domain.api

import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.model.images.ImagesReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetail
import com.zsoltbertalan.flickslate.tv.domain.model.TvShow
import kotlinx.coroutines.flow.Flow

interface TvRepository {

	fun getTopRatedTv(
		page: Int,
	): Flow<Outcome<PagingReply<TvShow>>>

	suspend fun getTvDetails(seriesId: Int): Outcome<TvDetail>
	suspend fun getTvImages(seriesId: Int): Outcome<ImagesReply>

}
