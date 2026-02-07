package com.zsoltbertalan.flickslate.tv.data.repository

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.data.getresult.backoffRetryPolicy
import com.zsoltbertalan.flickslate.shared.data.getresult.fetchCacheThenRemote
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.domain.model.images.ImagesReply
import com.zsoltbertalan.flickslate.tv.data.api.TvDataSource
import com.zsoltbertalan.flickslate.tv.domain.api.TvRepository
import com.zsoltbertalan.flickslate.tv.domain.model.SeasonDetail
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetail
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ContributesBinding(ActivityRetainedScope::class)
@SingleIn(ActivityRetainedScope::class)
class TvAccessor @Inject internal constructor(
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

	override suspend fun getTvDetails(seriesId: Int, sessionId: String?): Outcome<TvDetail> {
		return tvRemoteDataSource.getTvDetails(seriesId, sessionId)
	}

	override suspend fun getTvImages(seriesId: Int): Outcome<ImagesReply> {
		return tvRemoteDataSource.getTvImages(seriesId)
	}

	override suspend fun getTvSeasonDetail(seriesId: Int, seasonNumber: Int): Outcome<SeasonDetail> {
		return tvRemoteDataSource.getTvSeasonDetails(seriesId, seasonNumber)
	}

	override suspend fun getTvEpisodeDetail(
		seriesId: Int,
		seasonNumber: Int,
		episodeNumber: Int,
		sessionId: String?
	): Outcome<TvEpisodeDetail> {
		return tvRemoteDataSource.getTvEpisodeDetail(seriesId, seasonNumber, episodeNumber, sessionId)
	}

}
