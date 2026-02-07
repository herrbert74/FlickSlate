package com.zsoltbertalan.flickslate.tv.data.network

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.data.network.model.images.toImagesReply
import com.zsoltbertalan.flickslate.shared.data.network.model.toTvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.data.util.runCatchingApi
import com.zsoltbertalan.flickslate.shared.data.util.safeCall
import com.zsoltbertalan.flickslate.shared.data.util.safeCallWithMetadata
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.domain.model.images.ImagesReply
import com.zsoltbertalan.flickslate.tv.data.api.TvDataSource
import com.zsoltbertalan.flickslate.tv.data.network.model.TopRatedTvReplyDto
import com.zsoltbertalan.flickslate.tv.data.network.model.TvSeasonDetailsDto
import com.zsoltbertalan.flickslate.tv.data.network.model.toTvDetail
import com.zsoltbertalan.flickslate.tv.data.network.model.toTvSeasonDetails
import com.zsoltbertalan.flickslate.tv.data.network.model.toTvShowsReply
import com.zsoltbertalan.flickslate.tv.domain.model.SeasonDetail
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetail
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import retrofit2.Response

@ContributesBinding(ActivityRetainedScope::class)
@SingleIn(ActivityRetainedScope::class)
class TvRemoteDataSource @Inject internal constructor(
	private val tvService: TvService
) : TvDataSource.Remote {

	override suspend fun getTopRatedTv(etag: String?, page: Int?): Outcome<PagingReply<TvShow>> {
		return safeCallWithMetadata(
			{ tvService.getTopRatedTv(ifNoneMatch = etag, page = page) },
			Response<TopRatedTvReplyDto>::toTvShowsReply
		)
	}

	override suspend fun getTvSeasonDetails(tvId: Int, tvSeasonNumber: Int): Outcome<SeasonDetail> {
		return safeCall(
			{ tvService.getTvSeasonDetails(seriesId = tvId, seasonNumber = tvSeasonNumber) },
			TvSeasonDetailsDto::toTvSeasonDetails
		)
	}

	override suspend fun getTvDetails(seriesId: Int, sessionId: String?): Outcome<TvDetail> {
		return runCatchingApi {
			tvService
				.getTvDetails(seriesId = seriesId, sessionId = sessionId, appendToResponse = "account_states")
				.toTvDetail()
		}
	}

	override suspend fun getTvImages(seriesId: Int): Outcome<ImagesReply> {
		return runCatchingApi { tvService.getTvImages(tvId = seriesId).toImagesReply() }
	}

	override suspend fun getTvEpisodeDetail(
		seriesId: Int,
		seasonNumber: Int,
		episodeNumber: Int,
		sessionId: String?
	): Outcome<TvEpisodeDetail> {
		return runCatchingApi {
			tvService.getTvEpisodeDetails(
				seriesId = seriesId,
				seasonNumber = seasonNumber,
				episodeNumber = episodeNumber,
				sessionId = sessionId,
				appendToResponse = "account_states"
			).toTvEpisodeDetail()
		}
	}

}
