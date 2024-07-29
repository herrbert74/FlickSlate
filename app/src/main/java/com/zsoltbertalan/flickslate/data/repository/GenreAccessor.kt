package com.zsoltbertalan.flickslate.data.repository

import com.github.michaelbull.result.map
import com.zsoltbertalan.flickslate.data.db.GenreDataSource
import com.zsoltbertalan.flickslate.data.network.FlickSlateService
import com.zsoltbertalan.flickslate.data.network.dto.GenreResponse
import com.zsoltbertalan.flickslate.data.network.dto.toGenres
import com.zsoltbertalan.flickslate.data.network.dto.toMoviesResponse
import com.zsoltbertalan.flickslate.common.async.IoDispatcher
import com.zsoltbertalan.flickslate.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.domain.model.Genre
import com.zsoltbertalan.flickslate.common.util.Outcome
import com.zsoltbertalan.flickslate.common.util.runCatchingApi
import com.zsoltbertalan.flickslate.common.util.getresult.fetchCacheThenNetworkResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreAccessor @Inject constructor(
	private val flickSlateService: FlickSlateService,
	private val genreDataSource: GenreDataSource,
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
			mapper = GenreResponse::toGenres,
		).flowOn(dispatcher)
	}

	override fun getGenreDetail(
		genreId: Int,
	) = createPager { page ->
		flickSlateService.runCatchingApi {
			getGenreMovie(with_genres = genreId, page = page)
		}.map { Pair(it.toMoviesResponse().movies, it.total_pages ?: 0) }
	}.flow

}
