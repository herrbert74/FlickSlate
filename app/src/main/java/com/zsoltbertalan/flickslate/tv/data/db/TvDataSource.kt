package com.zsoltbertalan.flickslate.tv.data.db

import com.zsoltbertalan.flickslate.domain.model.PageData
import com.zsoltbertalan.flickslate.domain.model.PagingReply
import com.zsoltbertalan.flickslate.tv.domain.model.TvShow
import kotlinx.coroutines.flow.Flow

interface TvDataSource {

	suspend fun purgeDatabase()

	suspend fun insertTv(tvShowList: List<TvShow>, page: Int)

	suspend fun insertTvPageData(page: PageData)

	fun getTv(page: Int): Flow<PagingReply<TvShow>?>

}
