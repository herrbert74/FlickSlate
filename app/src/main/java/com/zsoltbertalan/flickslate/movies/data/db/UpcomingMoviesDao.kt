package com.zsoltbertalan.flickslate.movies.data.db

import com.zsoltbertalan.flickslate.shared.async.IoDispatcher
import com.zsoltbertalan.flickslate.shared.util.runCatchingUnit
import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.domain.model.PageData
import com.zsoltbertalan.flickslate.domain.model.PagingReply
import com.zsoltbertalan.flickslate.movies.data.db.model.UpcomingMovieDbo
import com.zsoltbertalan.flickslate.movies.data.db.model.UpcomingMoviesPageDbo
import com.zsoltbertalan.flickslate.movies.data.db.model.toMovie
import com.zsoltbertalan.flickslate.movies.data.db.model.toPageData
import com.zsoltbertalan.flickslate.movies.data.db.model.toUpcomingMoviesDbo
import com.zsoltbertalan.flickslate.movies.data.db.model.toUpcomingMoviesPageDbo
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpcomingMoviesDao @Inject constructor(
	private val realm: Realm,
	@IoDispatcher private val ioContext: CoroutineDispatcher,
) : UpcomingMoviesDataSource {

	override suspend fun purgeDatabase() {
		realm.write {
			val moviesToDelete = this.query(UpcomingMovieDbo::class).find()
			delete(moviesToDelete)
		}
	}

	override suspend fun insertUpcomingMovies(movies: List<Movie>, page: Int) {
		runCatchingUnit {
			realm.write {
				movies.map { copyToRealm(it.toUpcomingMoviesDbo(page), UpdatePolicy.ALL) }
			}
		}
	}

	override suspend fun insertUpcomingMoviesPageData(page: PageData) {
		runCatchingUnit {
			realm.write {
				copyToRealm(page.toUpcomingMoviesPageDbo(), UpdatePolicy.ALL)
			}
		}
	}

	override fun getUpcomingMovies(page: Int): Flow<PagingReply<Movie>?> {
		return realm.query(UpcomingMovieDbo::class, "page = $0", page).asFlow()
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
		return realm.query(UpcomingMoviesPageDbo::class, "page = $0", page).find()
			.map { dbo -> dbo.toPageData() }.firstOrNull()
	}

}
