package com.zsoltbertalan.flickslate

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.domain.model.images.ImagesReply
import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
import com.zsoltbertalan.flickslate.tv.domain.model.SeasonDetail
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetail
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetailWithImages
import com.zsoltbertalan.flickslate.tv.domain.model.TvMother
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeTvRepository : TvRepository {

	var tvDetail: TvDetailWithImages = TvMother.createTvDetailWithImages()
	var tvEpisodeDetail: TvEpisodeDetail = TvMother.createTvEpisodeDetail()

	override fun getTopRatedTv(page: Int): Flow<Outcome<PagingReply<TvShow>>> =
		flowOf(Ok(PagingReply(TvMother.createTvList(), true, PageData())))

	override suspend fun getTvDetails(seriesId: Int, sessionId: String?): Outcome<TvDetail> =
		Ok(
			TvMother.createTvDetail(
				personalRating = tvDetail.personalRating,
				favorite = tvDetail.favorite,
			)
		)

	override suspend fun getTvImages(seriesId: Int): Outcome<ImagesReply> = Ok(tvDetail.tvImages)

	override suspend fun getTvSeasonDetail(seriesId: Int, seasonNumber: Int): Outcome<SeasonDetail> =
		Ok(TvMother.createSeasonDetail(seriesId, seasonNumber))

	override suspend fun getTvEpisodeDetail(
		seriesId: Int,
		seasonNumber: Int,
		episodeNumber: Int,
		sessionId: String?
	): Outcome<TvEpisodeDetail> = Ok(tvEpisodeDetail)

}
