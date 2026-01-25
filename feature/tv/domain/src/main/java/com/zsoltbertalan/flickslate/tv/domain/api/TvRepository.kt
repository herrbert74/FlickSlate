package com.zsoltbertalan.flickslate.tv.domain.api

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.domain.model.images.ImagesReply
import com.zsoltbertalan.flickslate.tv.domain.model.SeasonDetail
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetail
import kotlinx.coroutines.flow.Flow

interface TvRepository {

	fun getTopRatedTv(
		page: Int,
	): Flow<Outcome<PagingReply<TvShow>>>

	suspend fun getTvDetails(seriesId: Int, sessionId: String? = null): Outcome<TvDetail>
	suspend fun getTvImages(seriesId: Int): Outcome<ImagesReply>
	suspend fun getTvSeasonDetail(seriesId: Int, seasonNumber: Int): Outcome<SeasonDetail>
	suspend fun getTvEpisodeDetail(
		seriesId: Int,
		seasonNumber: Int,
		episodeNumber: Int,
		sessionId: String? = null
	): Outcome<TvEpisodeDetail>

}
