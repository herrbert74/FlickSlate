package com.zsoltbertalan.flickslate.tv.data.api

import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.tv.domain.model.TvShow
import kotlinx.coroutines.flow.Flow

interface TvDataSource {

	interface Local {

		suspend fun purgeDatabase()

		suspend fun insertTv(tvShowList: List<TvShow>, page: Int)

		suspend fun insertTvPageData(page: PageData)

		fun getTv(page: Int): Flow<PagingReply<TvShow>?>

	}

	interface Remote {

		suspend fun getTopRatedTv(page: Int?): Outcome<PagingReply<TvShow>>

	}

}
