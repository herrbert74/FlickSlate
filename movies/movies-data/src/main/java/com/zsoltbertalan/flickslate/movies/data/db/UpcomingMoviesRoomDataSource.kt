package com.zsoltbertalan.flickslate.movies.data.db

import com.zsoltbertalan.flickslate.movies.data.api.UpcomingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.db.model.toMovie
import com.zsoltbertalan.flickslate.movies.data.db.model.toPageData
import com.zsoltbertalan.flickslate.movies.data.db.model.toUpcomingMoviesEntity
import com.zsoltbertalan.flickslate.movies.data.db.model.toUpcomingMoviesPageEntity
import com.zsoltbertalan.flickslate.shared.model.Movie
import com.zsoltbertalan.flickslate.shared.async.IoDispatcher
import com.zsoltbertalan.flickslate.shared.data.util.runCatchingUnit
import com.zsoltbertalan.flickslate.shared.model.PageData
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpcomingMoviesRoomDataSource @Inject constructor(
	private val moviesDatabase: MoviesDatabase,
	@IoDispatcher private val ioContext: CoroutineDispatcher,
) : UpcomingMoviesDataSource.Local {

	override suspend fun purgeDatabase() {
		runCatchingUnit {
			moviesDatabase.upcomingMoviesDao().purgeDatabase()
		}
	}

	override suspend fun insertUpcomingMovies(movies: List<Movie>, page: Int)  {
		Timber.d("zsoltbertalan* insertUpcomingMovies: $movies")
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
