package com.zsoltbertalan.flickslate.data.repository

import com.zsoltbertalan.flickslate.common.async.IoDispatcher
import com.zsoltbertalan.flickslate.common.util.Outcome
import com.zsoltbertalan.flickslate.data.db.GenreDataSource
import com.zsoltbertalan.flickslate.data.db.GenreMoviesDataSource
import com.zsoltbertalan.flickslate.data.network.FlickSlateService
import com.zsoltbertalan.flickslate.data.network.dto.GenreReplyDto
import com.zsoltbertalan.flickslate.data.network.dto.MoviesReplyDto
import com.zsoltbertalan.flickslate.data.network.dto.toGenres
import com.zsoltbertalan.flickslate.data.network.dto.toMoviesResponse
import com.zsoltbertalan.flickslate.data.repository.getresult.fetchCacheThenNetworkResponse
import com.zsoltbertalan.flickslate.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.domain.model.Genre
import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.domain.model.PageData
import com.zsoltbertalan.flickslate.domain.model.PagingReply
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreAccessor @Inject constructor(
	private val flickSlateService: FlickSlateService,
	private val genreDataSource: GenreDataSource,
	private val genreMoviesDataSource: GenreMoviesDataSource,
	@IoDispatcher val dispatcher: CoroutineDispatcher
) : GenreRepository {

	override fun getGenresList(): Flow<Outcome<List<Genre>>> {
		return fetchCacheThenNetworkResponse(
			fetchFromLocal = { genreDataSource.getGenres() },
			makeNetworkRequest = {
				val etag = genreDataSource.getEtag()
				flickSlateService.getGenres(ifNoneMatch = etag)
			},
			saveResponseData = { genreResponse ->
				val etag = genreResponse.headers()["etag"] ?: ""
				genreDataSource.insertEtag(etag)
				val genres = genreResponse.body()?.toGenres().orEmpty()
				genreDataSource.insertGenres(genres)
			},
			mapper = GenreReplyDto::toGenres,
		).flowOn(dispatcher)
	}

//	override fun getGenreDetail(
//		genreId: Int,
//	) = createPager { page ->
//		flickSlateService.runCatchingApi {
//			getGenreMovie(withGenres = genreId, page = page)
//		}.map { Pair(it.toMoviesResponse().movies, it.total_pages ?: 0) }
//	}.flow

	override fun getGenreDetail(genreId: Int, page: Int): Flow<Outcome<PagingReply<Movie>>> {
		return fetchCacheThenNetworkResponse(
			fetchFromLocal = { genreMoviesDataSource.getGenreMovies(page) },
			makeNetworkRequest = { flickSlateService.getGenreMovie(withGenres = genreId, page = page) },
			saveResponseData = { response ->
				val etag = response.headers()["etag"] ?: ""
				val moviesReply = response.body()?.toMoviesResponse()
				genreMoviesDataSource.insertGenreMoviesPageData(
					PageData(
						page,
						response.headers()["date"] ?: "",
						response.headers()["x-memc-expires"]?.toInt() ?: 0,
						etag,
						response.body()?.total_pages ?: 0,
						response.body()?.total_results ?: 0,
					)
				)
				genreMoviesDataSource.insertGenreMovies(moviesReply?.pagingList.orEmpty(), page)
			},
			mapper = MoviesReplyDto::toMoviesResponse,
		)
	}

}
