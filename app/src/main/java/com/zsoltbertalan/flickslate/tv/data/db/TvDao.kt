package com.zsoltbertalan.flickslate.tv.data.db

import com.zsoltbertalan.flickslate.shared.async.IoDispatcher
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.runCatchingUnit
import com.zsoltbertalan.flickslate.tv.data.api.TvDataSource
import com.zsoltbertalan.flickslate.tv.data.db.model.TvPageDbo
import com.zsoltbertalan.flickslate.tv.data.db.model.TvShowDbo
import com.zsoltbertalan.flickslate.tv.data.db.model.toMovie
import com.zsoltbertalan.flickslate.tv.data.db.model.toPageData
import com.zsoltbertalan.flickslate.tv.data.db.model.toTvDbo
import com.zsoltbertalan.flickslate.tv.data.db.model.toTvPageDbo
import com.zsoltbertalan.flickslate.tv.domain.model.TvShow
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvDao @Inject constructor(
	private val realm: Realm,
	@IoDispatcher private val ioContext: CoroutineDispatcher,
) : TvDataSource.Local {

	override suspend fun purgeDatabase() {
		realm.write {
			val moviesToDelete = this.query(TvShowDbo::class).find()
			delete(moviesToDelete)
		}
	}

	override suspend fun insertTv(tvShowList: List<TvShow>, page: Int) {
		runCatchingUnit {
			realm.write {
				tvShowList.map { copyToRealm(it.toTvDbo(page), UpdatePolicy.ALL) }
			}
		}
	}

	override suspend fun insertTvPageData(page: PageData) {
		runCatchingUnit {
			realm.write {
				copyToRealm(page.toTvPageDbo(), UpdatePolicy.ALL)
			}
		}
	}

	override fun getTv(page: Int): Flow<PagingReply<TvShow>?> {
		return realm.query(TvShowDbo::class, "page = $0", page).asFlow()
			.map { change ->
				val pageData = getPageData(page)
				val isLastPage = pageData?.totalPages == page
				val pagingList = change.list.map { it.toMovie() }.ifEmpty { null }
				pagingList?.let {
					PagingReply(pagingList, isLastPage, PageData())
				}
			}.flowOn(ioContext)
	}

	private fun getPageData(page: Int): PageData? {
		return realm.query(TvPageDbo::class, "page = $0", page).find()
			.map { dbo -> dbo.toPageData() }.firstOrNull()
	}

}
