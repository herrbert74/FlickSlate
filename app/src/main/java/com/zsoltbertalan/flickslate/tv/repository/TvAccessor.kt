package com.zsoltbertalan.flickslate.tv.repository

import com.zsoltbertalan.flickslate.tv.data.network.TvService
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.data.getresult.fetchCacheThenNetworkResponse
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.tv.data.db.TvDataSource
import com.zsoltbertalan.flickslate.tv.data.network.model.TopRatedTvReplyDto
import com.zsoltbertalan.flickslate.tv.data.network.model.toTvDetail
import com.zsoltbertalan.flickslate.tv.data.network.model.toTvList
import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetail
import com.zsoltbertalan.flickslate.tv.domain.model.TvShow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvAccessor @Inject constructor(
	private val tvService: TvService,
	private val tvDataSource: TvDataSource,
) : TvRepository {

	override fun getTopRatedTv(page: Int): Flow<Outcome<PagingReply<TvShow>>> {
		return fetchCacheThenNetworkResponse(
			fetchFromLocal = { tvDataSource.getTv(page) },
			makeNetworkRequest = { tvService.getTopRatedTv(page = page) },
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
		return com.zsoltbertalan.flickslate.shared.util.runCatchingApi {
			tvService.getTvDetails(seriesId = seriesId).toTvDetail()
		}
	}

}
