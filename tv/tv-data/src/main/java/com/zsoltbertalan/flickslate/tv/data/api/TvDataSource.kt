package com.zsoltbertalan.flickslate.tv.data.api

import com.zsoltbertalan.flickslate.shared.model.PageData
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.tv.domain.model.TvShow
import kotlinx.coroutines.flow.Flow

interface TvDataSource {

	interface Local {

		suspend fun purgeDatabase()

		suspend fun insertTv(tvShowList: List<TvShow>, page: Int)

		suspend fun insertTvPageData(page: PageData)

		fun getTv(page: Int): Flow<PagingReply<TvShow>?>

		suspend fun getEtag(page: Int): String?

	}

	interface Remote {

		suspend fun getTopRatedTv(etag: String? = null, page: Int?): Outcome<PagingReply<TvShow>>

	}

}
