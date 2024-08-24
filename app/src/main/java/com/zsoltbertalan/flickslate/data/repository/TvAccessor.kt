package com.zsoltbertalan.flickslate.data.repository

import com.zsoltbertalan.flickslate.common.util.Outcome
import com.zsoltbertalan.flickslate.data.db.TvDataSource
import com.zsoltbertalan.flickslate.data.network.FlickSlateService
import com.zsoltbertalan.flickslate.data.network.dto.TopRatedTvReplyDto
import com.zsoltbertalan.flickslate.data.network.dto.toTvDetail
import com.zsoltbertalan.flickslate.data.network.dto.toTvList
import com.zsoltbertalan.flickslate.data.repository.getresult.fetchCacheThenNetworkResponse
import com.zsoltbertalan.flickslate.domain.api.TvRepository
import com.zsoltbertalan.flickslate.domain.model.PageData
import com.zsoltbertalan.flickslate.domain.model.PagingReply
import com.zsoltbertalan.flickslate.domain.model.TvShow
import com.zsoltbertalan.flickslate.domain.model.TvDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvAccessor @Inject constructor(
	private val flickSlateService: FlickSlateService,
	private val tvDataSource: TvDataSource,
) : TvRepository {

	override fun getTopRatedTv(page: Int): Flow<Outcome<PagingReply<TvShow>>> {
		return fetchCacheThenNetworkResponse(
			fetchFromLocal = { tvDataSource.getTv(page) },
			makeNetworkRequest = { flickSlateService.getTopRatedTv(page = page) },
			saveResponseData = { response ->
				val etag = response.headers()["etag"] ?: ""
				val tvReply = response.body()?.toTvList()
				tvDataSource.insertTvPageData(
					PageData(
						page,
						response.headers()["date"] ?: "",
						response.headers()["x-memc-expires"]?.toInt() ?: 0,
						etag,
						response.body()?.total_pages ?: 0,
						response.body()?.total_results ?: 0,
					)
				)
				tvDataSource.insertTv(tvReply?.pagingList.orEmpty(), page)
			},
			mapper = TopRatedTvReplyDto::toTvList,
		)
	}
	override suspend fun getTvDetails(seriesId: Int): Outcome<TvDetail> {
		return com.zsoltbertalan.flickslate.common.util.runCatchingApi {
			flickSlateService.getTvDetails(seriesId = seriesId).toTvDetail()
		}
	}

}
