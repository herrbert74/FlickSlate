package com.zsoltbertalan.flickslate.movies.repository

import com.zsoltbertalan.flickslate.domain.model.PageData
import com.zsoltbertalan.flickslate.domain.model.PagingReply
import com.zsoltbertalan.flickslate.movies.data.db.NowPlayingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.db.PopularMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.db.UpcomingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.network.model.MoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.NowPlayingMoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.UpcomingMoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.toMovieDetail
import com.zsoltbertalan.flickslate.movies.data.network.model.toMoviesReply
import com.zsoltbertalan.flickslate.movies.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetail
import com.zsoltbertalan.flickslate.movies.network.MoviesService
import com.zsoltbertalan.flickslate.shared.data.getresult.fetchCacheThenNetworkResponse
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.shared.util.runCatchingApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesAccessor @Inject constructor(
	private val moviesService: MoviesService,
	private val popularMoviesDataSource: PopularMoviesDataSource,
	private val nowPlayingMoviesDataSource: NowPlayingMoviesDataSource,
	private val upcomingMoviesDataSource: UpcomingMoviesDataSource
) : MoviesRepository {

	/**
	 * This might have subtle bugs due to the shortcomings of the TMDB API:
	 * It returns the cached versions of the pages, which are retained for a day, but regenerated at different times.
	 * As a result, it can happen that two movies swap pages, but because only one of them is updated,
	 * one movie will be duplicated, the other will disappear.
	 * I haven't tested it, but this could break paging, where we scroll to the end of a long list,
	 * but because more than the threshold number of movies are missing, the fetch is not triggered.
	 * Similar problems are unresolved and unanswered on the TMDB forums, so I cannot be bothered to file an issue.
	 * The API doesn't respect the no-store header either.
	 */
	override fun getPopularMovies(page: Int): Flow<Outcome<PagingReply<Movie>>> {
		return fetchCacheThenNetworkResponse(
			fetchFromLocal = { popularMoviesDataSource.getPopularMovies(page) },
			makeNetworkRequest = { moviesService.getPopularMovies(page = page) },
			saveResponseData = { response ->
				val etag = response.headers()["etag"] ?: ""
				val moviesReply = response.body()?.toMoviesReply()
				popularMoviesDataSource.insertPopularMoviesPageData(
					PageData(
						page,
						response.headers()["date"] ?: "",
						response.headers()["x-memc-expires"]?.toInt() ?: 0,
						etag,
						response.body()?.total_pages ?: 0,
						response.body()?.total_results ?: 0,
					)
				)
				popularMoviesDataSource.insertPopularMovies(moviesReply?.pagingList.orEmpty(), page)
			},
			mapper = MoviesReplyDto::toMoviesReply,
		)
	}

	override fun getUpcomingMovies(page: Int): Flow<Outcome<PagingReply<Movie>>> {
		return fetchCacheThenNetworkResponse(
			fetchFromLocal = { upcomingMoviesDataSource.getUpcomingMovies(page) },
			makeNetworkRequest = { moviesService.getUpcomingMovies(page = page) },
			saveResponseData = { response ->
				val etag = response.headers()["etag"] ?: ""
				val moviesReply = response.body()?.toMoviesReply()
				upcomingMoviesDataSource.insertUpcomingMoviesPageData(
					PageData(
						page,
						response.headers()["date"] ?: "",
						response.headers()["x-memc-expires"]?.toInt() ?: 0,
						etag,
						response.body()?.total_pages ?: 0,
						response.body()?.total_results ?: 0,
					)
				)
				upcomingMoviesDataSource.insertUpcomingMovies(moviesReply?.pagingList.orEmpty(), page)
			},
			mapper = UpcomingMoviesReplyDto::toMoviesReply,
		)
	}

	override fun getNowPlayingMovies(page: Int): Flow<Outcome<PagingReply<Movie>>> {
		return fetchCacheThenNetworkResponse(
			fetchFromLocal = { nowPlayingMoviesDataSource.getNowPlayingMovies(page) },
			makeNetworkRequest = { moviesService.getNowPlayingMovies(page = page) },
			saveResponseData = { response ->
				val etag = response.headers()["etag"] ?: ""
				val moviesReply = response.body()?.toMoviesReply()
				nowPlayingMoviesDataSource.insertNowPlayingMoviesPageData(
					PageData(
						page,
						response.headers()["date"] ?: "",
						response.headers()["x-memc-expires"]?.toInt() ?: 0,
						etag,
						response.body()?.total_pages ?: 0,
						response.body()?.total_results ?: 0,
					)
				)
				nowPlayingMoviesDataSource.insertNowPlayingMovies(moviesReply?.pagingList.orEmpty(), page)
			},
			mapper = NowPlayingMoviesReplyDto::toMoviesReply,
		)
	}

	override suspend fun getMovieDetails(movieId: Int): Outcome<MovieDetail> {
		return moviesService.runCatchingApi {
			getMovieDetails(movieId = movieId).toMovieDetail()
		}
	}

}
