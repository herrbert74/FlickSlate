package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.movies.data.api.NowPlayingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.network.model.NowPlayingMoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.toMoviesReply
import com.zsoltbertalan.flickslate.shared.data.util.safeCallWithMetadata
import com.zsoltbertalan.flickslate.shared.model.Movie
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Response
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@ActivityRetainedScoped
class NowPlayingMoviesRemoteDataSource @Inject constructor(
	private val moviesService: MoviesService
) : NowPlayingMoviesDataSource.Remote {

	override suspend fun getNowPlayingMovies(etag: String?, page: Int?): Outcome<PagingReply<Movie>> {
		return safeCallWithMetadata(
			{ moviesService.getNowPlayingMovies(ifNoneMatch = etag, page = page) },
			Response<NowPlayingMoviesReplyDto>::toMoviesReply
		)
	}

}
