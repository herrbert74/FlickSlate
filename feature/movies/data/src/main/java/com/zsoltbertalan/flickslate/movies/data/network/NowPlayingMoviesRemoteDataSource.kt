package com.zsoltbertalan.flickslate.movies.data.network

import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.movies.data.api.NowPlayingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.network.model.NowPlayingMoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.toMoviesReply
import com.zsoltbertalan.flickslate.shared.data.util.safeCallWithMetadata
import com.zsoltbertalan.flickslate.shared.domain.di.ActivityRetainedScope
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import retrofit2.Response
import javax.inject.Inject

@ContributesBinding(ActivityRetainedScope::class)
@SingleIn(ActivityRetainedScope::class)
internal class NowPlayingMoviesRemoteDataSource @Inject constructor(
	private val moviesService: MoviesService
) : NowPlayingMoviesDataSource.Remote {

	override suspend fun getNowPlayingMovies(etag: String?, page: Int?, region: String?): Outcome<PagingReply<Movie>> {
		return safeCallWithMetadata(
			{ moviesService.getNowPlayingMovies(ifNoneMatch = etag, page = page, region = region) },
			Response<NowPlayingMoviesReplyDto>::toMoviesReply
		)
	}

}
