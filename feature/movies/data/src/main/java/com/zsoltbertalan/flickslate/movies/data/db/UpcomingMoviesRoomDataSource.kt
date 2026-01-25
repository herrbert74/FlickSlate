package com.zsoltbertalan.flickslate.movies.data.db

import com.zsoltbertalan.flickslate.movies.data.api.UpcomingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.db.model.toMovie
import com.zsoltbertalan.flickslate.movies.data.db.model.toPageData
import com.zsoltbertalan.flickslate.movies.data.db.model.toUpcomingMoviesEntity
import com.zsoltbertalan.flickslate.movies.data.db.model.toUpcomingMoviesPageEntity
import com.zsoltbertalan.flickslate.shared.data.util.runCatchingUnit
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.base.kotlin.async.IoDispatcher
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
internal class UpcomingMoviesRoomDataSource @Inject constructor(
	private val moviesDatabase: MoviesDatabase,
	@param:IoDispatcher private val ioContext: CoroutineDispatcher,
) : UpcomingMoviesDataSource.Local {

	override suspend fun purgeDatabase() {
		runCatchingUnit {
			moviesDatabase.upcomingMoviesDao().purgeDatabase()
		}
	}

	override suspend fun insertUpcomingMovies(movies: List<Movie>, page: Int) {
		withContext(ioContext) {
			runCatchingUnit {
				val m = movies.map { it.toUpcomingMoviesEntity(page) }
				moviesDatabase.upcomingMoviesDao().insertUpcomingMovies(m)
			}
		}
	}

	override suspend fun insertUpcomingMoviesPageData(page: PageData) {
		withContext(ioContext) {
			runCatchingUnit {
				moviesDatabase.upcomingMoviesPageDao().insertPageData(page.toUpcomingMoviesPageEntity())
			}
		}
	}

	override fun getUpcomingMovies(page: Int): Flow<PagingReply<Movie>?> =
		moviesDatabase.upcomingMoviesDao().getUpcomingMovies(page)
			.distinctUntilChanged()
			.map { change ->
				val pageData = getPageData(page)
				val isLastPage = pageData?.totalPages == page
				val pagingList = change.map { it.toMovie() }.ifEmpty { null }
				pagingList?.let {
					PagingReply(pagingList, isLastPage, PageData())
				}
			}.flowOn(ioContext)

	override suspend fun getEtag(page: Int): String? = withContext(ioContext) {
		return@withContext moviesDatabase.upcomingMoviesPageDao().getPageData(page).firstOrNull()?.etag
	}

	private fun getPageData(page: Int): PageData? =
		moviesDatabase.upcomingMoviesPageDao().getPageData(page).map { entity -> entity.toPageData() }.firstOrNull()

}
