package com.zsoltbertalan.flickslate.search.data.db

import com.zsoltbertalan.flickslate.search.data.api.GenreMoviesDataSource
import com.zsoltbertalan.flickslate.search.data.db.model.toGenreMoviesEntity
import com.zsoltbertalan.flickslate.search.data.db.model.toGenreMoviesPageEntity
import com.zsoltbertalan.flickslate.search.data.db.model.toMovie
import com.zsoltbertalan.flickslate.search.data.db.model.toPageData
import com.zsoltbertalan.flickslate.search.domain.api.model.GenreMoviesPagingReply
import com.zsoltbertalan.flickslate.shared.async.IoDispatcher
import com.zsoltbertalan.flickslate.shared.data.util.runCatchingUnit
import com.zsoltbertalan.flickslate.shared.model.Movie
import com.zsoltbertalan.flickslate.shared.model.PageData
import com.zsoltbertalan.flickslate.shared.model.PagingReply
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
class GenreMoviesRoomDataSource @Inject constructor(
	private val searchDatabase: SearchDatabase,
	@IoDispatcher private val ioContext: CoroutineDispatcher,
) : GenreMoviesDataSource.Local {

	override suspend fun purgeDatabase() {
		runCatchingUnit {
			searchDatabase.genreMoviesDao().purgeDatabase()
		}
	}

	override suspend fun insertGenreMovies(genreId: Int, movies: List<Movie>, page: Int) {
		withContext(ioContext) {
			runCatchingUnit {
				val m = movies.map { it.toGenreMoviesEntity(genreId, page) }
				searchDatabase.genreMoviesDao().insertGenreMovies(m)
			}
		}
	}

	override suspend fun insertGenreMoviesPageData(genreId: Int, page: PageData) {
		withContext(ioContext) {
			runCatchingUnit {
				searchDatabase.genreMoviesPageDao().insertPageData(page.toGenreMoviesPageEntity(genreId))
			}
		}
	}

	override fun getGenreMovies(genreId: Int, page: Int): Flow<GenreMoviesPagingReply?> =
		searchDatabase.genreMoviesDao().getGenreMovies(genreId, page)
			.distinctUntilChanged()
			.map { change ->
				val pageData = getPageData(page)
				val isLastPage = pageData?.totalPages == page
				val pagingList = change.map { it.toMovie() }.ifEmpty { null }
				pagingList?.let {
					GenreMoviesPagingReply(genreId, PagingReply(pagingList, isLastPage, PageData()))
				}
			}.flowOn(ioContext)

	override suspend fun getEtag(genreId: Int, page: Int): String? = withContext(ioContext) {
		return@withContext searchDatabase.genreMoviesPageDao().getPageData(page).firstOrNull()?.etag
	}

	private fun getPageData(page: Int): PageData? =
		searchDatabase.genreMoviesPageDao().getPageData(page).map { entity -> entity.toPageData() }.firstOrNull()

}
