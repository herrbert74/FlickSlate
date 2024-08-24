package com.zsoltbertalan.flickslate.data.db

import com.zsoltbertalan.flickslate.domain.model.PageData
import com.zsoltbertalan.flickslate.domain.model.PagingReply
import com.zsoltbertalan.flickslate.domain.model.Tv
import kotlinx.coroutines.flow.Flow

interface TvDataSource {

	suspend fun purgeDatabase()

	suspend fun insertTv(tvList: List<Tv>, page: Int)

	suspend fun insertTvPageData(page: PageData)

	fun getTv(page: Int): Flow<PagingReply<Tv>?>

}
