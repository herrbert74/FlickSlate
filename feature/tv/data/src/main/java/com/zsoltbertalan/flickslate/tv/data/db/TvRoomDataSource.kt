package com.zsoltbertalan.flickslate.tv.data.db

import com.zsoltbertalan.flickslate.shared.data.util.runCatchingUnit
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.kotlin.async.IoDispatcher
import com.zsoltbertalan.flickslate.tv.data.api.TvDataSource
import com.zsoltbertalan.flickslate.tv.data.db.model.toPageData
import com.zsoltbertalan.flickslate.tv.data.db.model.toTvEntity
import com.zsoltbertalan.flickslate.tv.data.db.model.toTvPageEntity
import com.zsoltbertalan.flickslate.tv.data.db.model.toTvShow
import com.zsoltbertalan.flickslate.tv.domain.model.TvShow
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ViewModelScoped
internal class TvRoomDataSource @Inject constructor(
	private val tvDatabase: TvDatabase,
	@param:IoDispatcher private val ioContext: CoroutineDispatcher,
) : TvDataSource.Local {

	override suspend fun purgeDatabase() {
		runCatchingUnit {
			tvDatabase.tvShowsDao().purgeDatabase()
		}
	}

	override suspend fun insertTv(tvShowList: List<TvShow>, page: Int) {
		withContext(ioContext) {
			runCatchingUnit {
				val m = tvShowList.map { it.toTvEntity(page) }
				tvDatabase.tvShowsDao().insertTvShows(m)
			}
		}
	}

	override suspend fun insertTvPageData(page: PageData) {
		withContext(ioContext) {
			runCatchingUnit {
				tvDatabase.tvShowsPageDao().insertPageData(page.toTvPageEntity())
			}
		}
	}

	override fun getTv(page: Int): Flow<PagingReply<TvShow>?> =
		tvDatabase.tvShowsDao().getTvShows(page)
			.distinctUntilChanged()
			.map { change ->
				val pageData = getPageData(page)
				val isLastPage = pageData?.totalPages == page
				val pagingList = change.map { it.toTvShow() }.ifEmpty { null }
				pagingList?.let {
					PagingReply(pagingList, isLastPage, PageData())
				}
			}.flowOn(ioContext)

	override suspend fun getEtag(page: Int): String? = withContext(ioContext) {
		return@withContext tvDatabase.tvShowsPageDao().getPageData(page).firstOrNull()?.etag
	}

	private fun getPageData(page: Int): PageData? =
		tvDatabase.tvShowsPageDao().getPageData(page).map { entity -> entity.toPageData() }.firstOrNull()

}
