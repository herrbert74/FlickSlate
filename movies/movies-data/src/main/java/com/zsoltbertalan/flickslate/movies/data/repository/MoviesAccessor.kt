package com.zsoltbertalan.flickslate.movies.data.repository

import com.zsoltbertalan.flickslate.movies.data.api.NowPlayingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.api.PopularMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.api.UpcomingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.network.MoviesService
import com.zsoltbertalan.flickslate.movies.data.network.model.images.toMovieImages
import com.zsoltbertalan.flickslate.movies.data.network.model.toMovieDetail
import com.zsoltbertalan.flickslate.movies.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetail
import com.zsoltbertalan.flickslate.movies.domain.model.images.MovieImages
import com.zsoltbertalan.flickslate.shared.data.getresult.backoffRetryPolicy
import com.zsoltbertalan.flickslate.shared.data.getresult.fetchCacheThenRemote
import com.zsoltbertalan.flickslate.shared.data.util.runCatchingApi
import com.zsoltbertalan.flickslate.shared.model.Movie
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
class MoviesAccessor @Inject constructor(
	private val moviesService: MoviesService,
	private val popularMoviesDataSource: PopularMoviesDataSource.Local,
	private val popularMoviesRemoteDataSource: PopularMoviesDataSource.Remote,
	private val nowPlayingMoviesDataSource: NowPlayingMoviesDataSource.Local,
	private val nowPlayingMoviesRemoteDataSource: NowPlayingMoviesDataSource.Remote,
	private val upcomingMoviesDataSource: UpcomingMoviesDataSource.Local,
	private val upcomingMoviesRemoteDataSource: UpcomingMoviesDataSource.Remote,
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
		return fetchCacheThenRemote(
			fetchFromLocal = { popularMoviesDataSource.getPopularMovies(page) },
			makeNetworkRequest = {
				val etag = popularMoviesDataSource.getEtag(page)
				popularMoviesRemoteDataSource.getPopularMovies(etag = etag, page = page)
			},
			saveResponseData = { pagingReply ->
				val moviesReply = pagingReply.pagingList
				popularMoviesDataSource.insertPopularMoviesPageData(
					pagingReply.pageData
				)
				popularMoviesDataSource.insertPopularMovies(moviesReply, page)
			},
			retryPolicy = backoffRetryPolicy,
		)
	}

	override fun getUpcomingMovies(page: Int): Flow<Outcome<PagingReply<Movie>>> {
		return fetchCacheThenRemote(
			fetchFromLocal = { upcomingMoviesDataSource.getUpcomingMovies(page) },
			makeNetworkRequest = {
				val etag = upcomingMoviesDataSource.getEtag(page)
				upcomingMoviesRemoteDataSource.getUpcomingMovies(etag = etag, page = page)
			},
			saveResponseData = { pagingReply ->
				val moviesReply = pagingReply.pagingList
				upcomingMoviesDataSource.insertUpcomingMoviesPageData(
					pagingReply.pageData
				)
				upcomingMoviesDataSource.insertUpcomingMovies(moviesReply, page)
			},
			retryPolicy = backoffRetryPolicy,
		)
	}

	override fun getNowPlayingMovies(page: Int): Flow<Outcome<PagingReply<Movie>>> {
		return fetchCacheThenRemote(
			fetchFromLocal = { nowPlayingMoviesDataSource.getNowPlayingMovies(page) },
			makeNetworkRequest = {
				val etag = nowPlayingMoviesDataSource.getEtag(page)
				nowPlayingMoviesRemoteDataSource.getNowPlayingMovies(etag = etag, page = page)
			},
			saveResponseData = { pagingReply ->
				val moviesReply = pagingReply.pagingList
				nowPlayingMoviesDataSource.insertNowPlayingMoviesPageData(
					pagingReply.pageData
				)
				nowPlayingMoviesDataSource.insertNowPlayingMovies(moviesReply, page)
			},
			retryPolicy = backoffRetryPolicy,
		)
	}

	override suspend fun getMovieDetails(movieId: Int): Outcome<MovieDetail> {
		return moviesService.runCatchingApi {
			getMovieDetails(movieId = movieId).toMovieDetail()
		}
	}

	override suspend fun getMovieImages(movieId: Int): Outcome<MovieImages> {
		return moviesService.runCatchingApi {
			getMovieImages(movieId = movieId).toMovieImages()
		}
	}

}
