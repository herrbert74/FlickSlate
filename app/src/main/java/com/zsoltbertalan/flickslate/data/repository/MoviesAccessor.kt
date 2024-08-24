package com.zsoltbertalan.flickslate.data.repository

import com.zsoltbertalan.flickslate.common.util.Outcome
import com.zsoltbertalan.flickslate.common.util.runCatchingApi
import com.zsoltbertalan.flickslate.data.db.NowPlayingMoviesDataSource
import com.zsoltbertalan.flickslate.data.db.PopularMoviesDataSource
import com.zsoltbertalan.flickslate.data.db.UpcomingMoviesDataSource
import com.zsoltbertalan.flickslate.data.network.FlickSlateService
import com.zsoltbertalan.flickslate.data.network.dto.MoviesResponseDto
import com.zsoltbertalan.flickslate.data.network.dto.NowPlayingMoviesResponse
import com.zsoltbertalan.flickslate.data.network.dto.UpcomingMoviesResponse
import com.zsoltbertalan.flickslate.data.network.dto.toMovieDetail
import com.zsoltbertalan.flickslate.data.network.dto.toMoviesResponse
import com.zsoltbertalan.flickslate.data.repository.getresult.fetchCacheThenNetworkResponse
import com.zsoltbertalan.flickslate.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.domain.model.MovieDetail
import com.zsoltbertalan.flickslate.domain.model.PageData
import com.zsoltbertalan.flickslate.domain.model.PagingReply
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesAccessor @Inject constructor(
	private val flickSlateService: FlickSlateService,
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
			makeNetworkRequest = { flickSlateService.getPopularMovies(page = page) },
			saveResponseData = { response ->
				val etag = response.headers()["etag"] ?: ""
				val moviesReply = response.body()?.toMoviesResponse()
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
			mapper = MoviesResponseDto::toMoviesResponse,
		)
	}

	override fun getUpcomingMovies(page: Int): Flow<Outcome<PagingReply<Movie>>> {
		return fetchCacheThenNetworkResponse(
			fetchFromLocal = { upcomingMoviesDataSource.getUpcomingMovies(page) },
			makeNetworkRequest = { flickSlateService.getUpcomingMovies(page = page) },
			saveResponseData = { response ->
				val etag = response.headers()["etag"] ?: ""
				val moviesReply = response.body()?.toMoviesResponse()
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
			mapper = UpcomingMoviesResponse::toMoviesResponse,
		)
	}

	override fun getNowPlayingMovies(page: Int): Flow<Outcome<PagingReply<Movie>>> {
		return fetchCacheThenNetworkResponse(
			fetchFromLocal = { nowPlayingMoviesDataSource.getNowPlayingMovies(page) },
			makeNetworkRequest = { flickSlateService.getNowPlayingMovies(page = page) },
			saveResponseData = { response ->
				val etag = response.headers()["etag"] ?: ""
				val moviesReply = response.body()?.toMoviesResponse()
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
			mapper = NowPlayingMoviesResponse::toMoviesResponse,
		)
	}

	override suspend fun getMovieDetails(movieId: Int): Outcome<MovieDetail> {
		return flickSlateService.runCatchingApi {
			getMovieDetails(movieId = movieId).toMovieDetail()
		}
	}

}
