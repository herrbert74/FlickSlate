package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.movies.data.api.NowPlayingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.network.model.NowPlayingMoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.toMoviesReply
import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.shared.util.safeCall
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NowPlayingRemoteDataSource @Inject constructor(
	private val moviesService: MoviesService
) : NowPlayingMoviesDataSource.Remote {

	override suspend fun getNowPlayingMovies(page: Int?): Outcome<PagingReply<Movie>> {
		return safeCall(
			{ moviesService.getNowPlayingMovies(page = page) },
			Response<NowPlayingMoviesReplyDto>::toMoviesReply
		)
	}

}
