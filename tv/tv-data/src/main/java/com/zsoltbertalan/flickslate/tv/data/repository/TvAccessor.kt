package com.zsoltbertalan.flickslate.tv.data.repository

import com.zsoltbertalan.flickslate.shared.data.getresult.backoffRetryPolicy
import com.zsoltbertalan.flickslate.shared.data.getresult.fetchCacheThenRemote
import com.zsoltbertalan.flickslate.shared.data.network.model.images.toImagesReply
import com.zsoltbertalan.flickslate.shared.data.util.runCatchingApi
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.model.images.ImagesReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.tv.data.api.TvDataSource
import com.zsoltbertalan.flickslate.tv.data.network.TvService
import com.zsoltbertalan.flickslate.tv.data.network.model.toTvDetail
import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetail
import com.zsoltbertalan.flickslate.tv.domain.model.TvShow
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ViewModelScoped
class TvAccessor @Inject constructor(
	private val tvService: TvService,
	private val tvDataSource: TvDataSource.Local,
	private val tvRemoteDataSource: TvDataSource.Remote,
) : TvRepository {

	override fun getTopRatedTv(page: Int): Flow<Outcome<PagingReply<TvShow>>> {
		return fetchCacheThenRemote(
			fetchFromLocal = { tvDataSource.getTv(page) },
			makeNetworkRequest = {
				val etag = tvDataSource.getEtag(page)
				tvRemoteDataSource.getTopRatedTv(etag = etag, page = page)
			},
			saveResponseData = { pagingReply ->
				val topRatedTvReply = pagingReply.pagingList
				tvDataSource.insertTvPageData(
					pagingReply.pageData
				)
				tvDataSource.insertTv(topRatedTvReply, page)
			},
			retryPolicy = backoffRetryPolicy,
		)
	}

	override suspend fun getTvDetails(seriesId: Int): Outcome<TvDetail> {
		return runCatchingApi {
			tvService.getTvDetails(seriesId = seriesId).toTvDetail()
		}
	}

	override suspend fun getTvImages(seriesId: Int): Outcome<ImagesReply> {
		return tvService.runCatchingApi {
			getTvImages(seriesId).toImagesReply()
		}
	}

}
