package com.zsoltbertalan.flickslate.movies.data.db

import com.zsoltbertalan.flickslate.movies.data.api.PopularMoviesDataSource
import com.zsoltbertalan.flickslate.shared.async.IoDispatcher
import com.zsoltbertalan.flickslate.shared.util.runCatchingUnit
import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.movies.data.db.model.PopularMovieDbo
import com.zsoltbertalan.flickslate.movies.data.db.model.PopularMoviesPageDbo
import com.zsoltbertalan.flickslate.movies.data.db.model.toMovie
import com.zsoltbertalan.flickslate.movies.data.db.model.toPageData
import com.zsoltbertalan.flickslate.movies.data.db.model.toPopularMoviesDbo
import com.zsoltbertalan.flickslate.movies.data.db.model.toPopularMoviesPageDbo
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PopularMoviesDao @Inject constructor(
	private val realm: Realm,
	@IoDispatcher private val ioContext: CoroutineDispatcher,
) : PopularMoviesDataSource.Local {

	override suspend fun purgeDatabase() {
		realm.write {
			val moviesToDelete = this.query(PopularMovieDbo::class).find()
			delete(moviesToDelete)
		}
	}

	override suspend fun insertPopularMovies(movies: List<Movie>, page: Int) {
		runCatchingUnit {
			realm.write {
				movies.map { copyToRealm(it.toPopularMoviesDbo(page), UpdatePolicy.ALL) }
			}
		}
	}

	override suspend fun insertPopularMoviesPageData(page: PageData) {
		runCatchingUnit {
			realm.write {
				copyToRealm(page.toPopularMoviesPageDbo(), UpdatePolicy.ALL)
			}
		}
	}

	override fun getPopularMovies(page: Int): Flow<PagingReply<Movie>?> {
		return realm.query(PopularMovieDbo::class, "page = $0", page).asFlow()
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
		return realm.query(PopularMoviesPageDbo::class, "page = $0", page).find()
			.map { dbo -> dbo.toPageData() }.firstOrNull()
	}

}
