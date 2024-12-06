package com.zsoltbertalan.flickslate.movies.data.db

import com.zsoltbertalan.flickslate.movies.data.api.NowPlayingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.db.model.toMovie
import com.zsoltbertalan.flickslate.movies.data.db.model.toNowPlayingMoviesEntity
import com.zsoltbertalan.flickslate.movies.data.db.model.toNowPlayingMoviesPageEntity
import com.zsoltbertalan.flickslate.movies.data.db.model.toPageData
import com.zsoltbertalan.flickslate.shared.async.IoDispatcher
import com.zsoltbertalan.flickslate.shared.data.util.runCatchingUnit
import com.zsoltbertalan.flickslate.shared.model.Movie
import com.zsoltbertalan.flickslate.shared.model.PageData
import com.zsoltbertalan.flickslate.shared.model.PagingReply
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
class NowPlayingMoviesRoomDataSource @Inject constructor(
	private val moviesDatabase: MoviesDatabase,
	@IoDispatcher private val ioContext: CoroutineDispatcher,
) : NowPlayingMoviesDataSource.Local {

	override suspend fun purgeDatabase() {
		runCatchingUnit {
			moviesDatabase.nowPlayingMoviesDao().purgeDatabase()
		}
	}

	override suspend fun insertNowPlayingMovies(movies: List<Movie>, page: Int)  {
		withContext(ioContext) {
			runCatchingUnit {
				val m = movies.map { it.toNowPlayingMoviesEntity(page) }
				moviesDatabase.nowPlayingMoviesDao().insertNowPlayingMovies(m)
			}
		}
	}

	override suspend fun insertNowPlayingMoviesPageData(page: PageData) {
		withContext(ioContext) {
			runCatchingUnit {
				moviesDatabase.nowPlayingMoviesPageDao().insertPageData(page.toNowPlayingMoviesPageEntity())
			}
		}
	}

	override fun getNowPlayingMovies(page: Int): Flow<PagingReply<Movie>?> =
		moviesDatabase.nowPlayingMoviesDao().getNowPlayingMovies(page)
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
		return@withContext moviesDatabase.nowPlayingMoviesPageDao().getPageData(page).firstOrNull()?.etag
	}

	private fun getPageData(page: Int): PageData? =
		moviesDatabase.nowPlayingMoviesPageDao().getPageData(page).map { entity -> entity.toPageData() }.firstOrNull()

}
