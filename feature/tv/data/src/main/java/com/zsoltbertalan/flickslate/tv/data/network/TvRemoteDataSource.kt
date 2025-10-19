package com.zsoltbertalan.flickslate.tv.data.network

import com.zsoltbertalan.flickslate.shared.data.util.safeCall
import com.zsoltbertalan.flickslate.shared.data.util.safeCallWithMetadata
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.tv.data.api.TvDataSource
import com.zsoltbertalan.flickslate.tv.data.network.model.TopRatedTvReplyDto
import com.zsoltbertalan.flickslate.tv.data.network.model.TvSeasonDetailsDto
import com.zsoltbertalan.flickslate.tv.data.network.model.toTvSeasonDetails
import com.zsoltbertalan.flickslate.tv.data.network.model.toTvShowsReply
import com.zsoltbertalan.flickslate.tv.domain.model.SeasonDetail
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Response
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ViewModelScoped
internal class TvRemoteDataSource @Inject constructor(
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

}
