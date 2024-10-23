package com.zsoltbertalan.flickslate.tv.repository

import com.zsoltbertalan.flickslate.tv.data.network.TvService
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.data.getresult.fetchCacheThenRemote
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.tv.data.api.TvDataSource
import com.zsoltbertalan.flickslate.tv.data.network.model.toTvDetail
import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetail
import com.zsoltbertalan.flickslate.tv.domain.model.TvShow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvAccessor @Inject constructor(
	private val tvService: TvService,
	private val tvDataSource: TvDataSource.Local,
	private val tvRemoteDataSource: TvDataSource.Remote,
) : TvRepository {

	override fun getTopRatedTv(page: Int): Flow<Outcome<PagingReply<TvShow>>> {
		return fetchCacheThenRemote(
			fetchFromLocal = { tvDataSource.getTv(page) },
			makeNetworkRequest = { tvRemoteDataSource.getTopRatedTv(page = page) },
			saveResponseData = { pagingReply ->
				val topRatedTvReply = pagingReply.pagingList
				tvDataSource.insertTvPageData(
					pagingReply.pageData
				)
				tvDataSource.insertTv(topRatedTvReply, page)
			},
		)
	}

	override suspend fun getTvDetails(seriesId: Int): Outcome<TvDetail> {
		return com.zsoltbertalan.flickslate.shared.util.runCatchingApi {
			tvService.getTvDetails(seriesId = seriesId).toTvDetail()
		}
	}

}
