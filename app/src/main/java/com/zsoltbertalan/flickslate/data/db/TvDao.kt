package com.zsoltbertalan.flickslate.data.db

import com.zsoltbertalan.flickslate.common.async.IoDispatcher
import com.zsoltbertalan.flickslate.common.util.runCatchingUnit
import com.zsoltbertalan.flickslate.domain.model.PageData
import com.zsoltbertalan.flickslate.domain.model.PagingReply
import com.zsoltbertalan.flickslate.domain.model.Tv
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
) : TvDataSource {

	override suspend fun purgeDatabase() {
		realm.write {
			val moviesToDelete = this.query(TvDbo::class).find()
			delete(moviesToDelete)
		}
	}

	override suspend fun insertTv(tvList: List<Tv>, page: Int) {
		runCatchingUnit {
			realm.write {
				tvList.map { copyToRealm(it.toTvDbo(page), UpdatePolicy.ALL) }
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

	override fun getTv(page: Int): Flow<PagingReply<Tv>?> {
		return realm.query(TvDbo::class, "page = $0", page).asFlow()
			.map { change ->
				val pageData = getPageData(page)
				val isLastPage = pageData?.totalPages == page
				val pagingList = change.list.map { it.toMovie() }.ifEmpty { null }
				pagingList?.let {
					PagingReply(pagingList, isLastPage)
				}
			}.flowOn(ioContext)
	}

	private fun getPageData(page: Int): PageData? {
		return realm.query(TvPageDbo::class, "page = $0", page).find()
			.map { dbo -> dbo.toPageData() }.firstOrNull()
	}

}
