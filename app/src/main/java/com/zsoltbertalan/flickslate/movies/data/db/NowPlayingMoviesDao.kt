package com.zsoltbertalan.flickslate.movies.data.db

import com.zsoltbertalan.flickslate.movies.data.api.NowPlayingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.db.model.NowPlayingMovieDbo
import com.zsoltbertalan.flickslate.movies.data.db.model.NowPlayingMoviesPageDbo
import com.zsoltbertalan.flickslate.movies.data.db.model.toMovie
import com.zsoltbertalan.flickslate.movies.data.db.model.toNowPlayingMoviesDbo
import com.zsoltbertalan.flickslate.movies.data.db.model.toNowPlayingMoviesPageDbo
import com.zsoltbertalan.flickslate.movies.data.db.model.toPageData
import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.async.IoDispatcher
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.runCatchingUnit
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NowPlayingMoviesDao @Inject constructor(
	private val realm: Realm,
	@IoDispatcher private val ioContext: CoroutineDispatcher,
) : NowPlayingMoviesDataSource.Local {

	override suspend fun purgeDatabase() {
		realm.write {
			val moviesToDelete = this.query(NowPlayingMovieDbo::class).find()
			delete(moviesToDelete)
		}
	}

	override suspend fun insertNowPlayingMovies(movies: List<Movie>, page: Int) {
		runCatchingUnit {
			realm.write {
				movies.map { copyToRealm(it.toNowPlayingMoviesDbo(page), UpdatePolicy.ALL) }
			}
		}
	}

	override suspend fun insertNowPlayingMoviesPageData(page: PageData) {
		runCatchingUnit {
			realm.write {
				copyToRealm(page.toNowPlayingMoviesPageDbo(), UpdatePolicy.ALL)
			}
		}
	}

	override fun getNowPlayingMovies(page: Int): Flow<PagingReply<Movie>?> {
		return realm.query(NowPlayingMovieDbo::class, "page = $0", page).asFlow()
			.map { change ->
				val pageData = getPageData(page)
				val isLastPage = pageData?.totalPages == page
				val pagingList = change.list.map { it.toMovie() }.ifEmpty { null }
				pagingList?.let {
					PagingReply(pagingList, isLastPage, pageData ?: PageData())
				}
			}.flowOn(ioContext)
	}

	override suspend fun getEtag(page: Int): String = withContext(ioContext) {
		return@withContext realm.query<NowPlayingMoviesPageDbo>("page = $0", page).first().find()?.etag ?: ""
	}

	private fun getPageData(page: Int): PageData? {
		return realm.query(NowPlayingMoviesPageDbo::class, "page = $0", page).find()
			.map { dbo -> dbo.toPageData() }.firstOrNull()
	}

}
