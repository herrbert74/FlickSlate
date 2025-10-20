package com.zsoltbertalan.flickslate.tv.data.api

import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.tv.domain.model.SeasonDetail
import kotlinx.coroutines.flow.Flow

internal interface TvDataSource {

	interface Local {

		suspend fun purgeDatabase()

		suspend fun insertTv(tvShowList: List<TvShow>, page: Int)

		suspend fun insertTvPageData(page: PageData)

		fun getTv(page: Int): Flow<PagingReply<TvShow>?>

		suspend fun getEtag(page: Int): String?

	}

	interface Remote {

		suspend fun getTopRatedTv(etag: String? = null, page: Int?): Outcome<PagingReply<TvShow>>

		suspend fun getTvSeasonDetails(tvId: Int, tvSeasonNumber: Int): Outcome<SeasonDetail>

	}

}
