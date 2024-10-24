package com.zsoltbertalan.flickslate.tv.data.network

import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.shared.util.safeCallWithMetadata
import com.zsoltbertalan.flickslate.tv.data.api.TvDataSource
import com.zsoltbertalan.flickslate.tv.data.network.model.TopRatedTvReplyDto
import com.zsoltbertalan.flickslate.tv.data.network.model.toTvShowsReply
import com.zsoltbertalan.flickslate.tv.domain.model.TvShow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvRemoteDataSource @Inject constructor(
	private val tvService: TvService
) : TvDataSource.Remote {

	override suspend fun getTopRatedTv(page: Int?): Outcome<PagingReply<TvShow>> {
		return safeCallWithMetadata(
			{ tvService.getTopRatedTv(page = page) },
			Response<TopRatedTvReplyDto>::toTvShowsReply
		)
	}

}
