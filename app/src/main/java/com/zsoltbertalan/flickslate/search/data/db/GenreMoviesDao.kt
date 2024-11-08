package com.zsoltbertalan.flickslate.search.data.db

import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.search.data.api.GenreMoviesDataSource
import com.zsoltbertalan.flickslate.search.data.db.model.GenreMovieDbo
import com.zsoltbertalan.flickslate.search.data.db.model.GenreMoviesPageDbo
import com.zsoltbertalan.flickslate.search.data.db.model.toGenreMoviesDbo
import com.zsoltbertalan.flickslate.search.data.db.model.toGenreMoviesPageDbo
import com.zsoltbertalan.flickslate.search.data.db.model.toMovie
import com.zsoltbertalan.flickslate.search.data.db.model.toPageData
import com.zsoltbertalan.flickslate.search.domain.api.model.GenreMoviesPagingReply
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
class GenreMoviesDao @Inject constructor(
	private val realm: Realm,
	@IoDispatcher private val ioContext: CoroutineDispatcher,
) : GenreMoviesDataSource.Local {

	override suspend fun purgeDatabase() {
		realm.write {
			val moviesToDelete = this.query(GenreMovieDbo::class).find()
			delete(moviesToDelete)
		}
	}

	override suspend fun insertGenreMovies(genreId: Int, movies: List<Movie>, page: Int) {
		runCatchingUnit {
			realm.write {
				movies.map { copyToRealm(it.toGenreMoviesDbo(genreId, page), UpdatePolicy.ALL) }
			}
		}
	}

	override suspend fun getEtag(genreId: Int, page: Int): String? = withContext(ioContext) {
		return@withContext realm.query<GenreMoviesPageDbo>(
			"genreIdAndPage = $0", "${genreId}_${page}"
		)
			.first().find()?.etag
	}

	override suspend fun insertGenreMoviesPageData(genreId: Int, page: PageData) {
		runCatchingUnit {
			realm.write {
				copyToRealm(page.toGenreMoviesPageDbo(genreId), UpdatePolicy.ALL)
			}
		}
	}

	override fun getGenreMovies(genreId: Int, page: Int): Flow<GenreMoviesPagingReply?> {
		return realm.query(GenreMovieDbo::class, "idAndGenreId ENDSWITH $0 && page = $1", "_${genreId}", page)
			.asFlow()
			.map { change ->
				val pageData = getPageData(genreId, page)
				val isLastPage = pageData?.totalPages == page
				val pagingList = change.list.map { it.toMovie() }.ifEmpty { null }
				pagingList?.let {
					GenreMoviesPagingReply(genreId, PagingReply(pagingList, isLastPage, PageData()))
				}
			}.flowOn(ioContext)
	}

	private fun getPageData(genreId: Int, page: Int): PageData? {
		return realm.query(GenreMoviesPageDbo::class, "genreIdAndPage = $0", "${genreId}_${page}", page).find()
			.map { dbo -> dbo.toPageData() }.firstOrNull()
	}

}
