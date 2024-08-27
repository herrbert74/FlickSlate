package com.zsoltbertalan.flickslate.search.data.db

import com.zsoltbertalan.flickslate.shared.async.IoDispatcher
import com.zsoltbertalan.flickslate.shared.util.runCatchingUnit
import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.search.data.db.model.GenreMovieDbo
import com.zsoltbertalan.flickslate.search.data.db.model.GenreMoviesPageDbo
import com.zsoltbertalan.flickslate.search.data.db.model.toGenreMoviesDbo
import com.zsoltbertalan.flickslate.search.data.db.model.toGenreMoviesPageDbo
import com.zsoltbertalan.flickslate.search.data.db.model.toMovie
import com.zsoltbertalan.flickslate.search.data.db.model.toPageData
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreMoviesDao @Inject constructor(
	private val realm: Realm,
	@IoDispatcher private val ioContext: CoroutineDispatcher,
) : GenreMoviesDataSource {

	override suspend fun purgeDatabase() {
		realm.write {
			val moviesToDelete = this.query(GenreMovieDbo::class).find()
			delete(moviesToDelete)
		}
	}

	override suspend fun insertGenreMovies(movies: List<Movie>, page: Int) {
		runCatchingUnit {
			realm.write {
				movies.map { copyToRealm(it.toGenreMoviesDbo(page), UpdatePolicy.ALL) }
			}
		}
	}

	override suspend fun insertGenreMoviesPageData(page: PageData) {
		runCatchingUnit {
			realm.write {
				copyToRealm(page.toGenreMoviesPageDbo(), UpdatePolicy.ALL)
			}
		}
	}

	override fun getGenreMovies(page: Int): Flow<PagingReply<Movie>?> {
		return realm.query(GenreMovieDbo::class, "page = $0", page).asFlow()
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
		return realm.query(GenreMoviesPageDbo::class, "page = $0", page).find()
			.map { dbo -> dbo.toPageData() }.firstOrNull()
	}

}
