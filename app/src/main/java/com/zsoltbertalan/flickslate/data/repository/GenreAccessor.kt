package com.zsoltbertalan.flickslate.data.repository

import com.github.michaelbull.result.map
import com.zsoltbertalan.flickslate.data.db.GenreDataSource
import com.zsoltbertalan.flickslate.data.network.FlickSlateService
import com.zsoltbertalan.flickslate.data.network.dto.GenreResponse
import com.zsoltbertalan.flickslate.data.network.dto.toGenres
import com.zsoltbertalan.flickslate.data.network.dto.toMoviesResponse
import com.zsoltbertalan.flickslate.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.domain.model.Genre
import com.zsoltbertalan.flickslate.ext.ApiResult
import com.zsoltbertalan.flickslate.ext.apiRunCatching
import com.zsoltbertalan.flickslate.util.getresult.fetchCacheThenNetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreAccessor @Inject constructor(
	private val flickSlateService: FlickSlateService,
	private val genreDataSource: GenreDataSource,
) : GenreRepository {

	override suspend fun getGenresList(): Flow<ApiResult<List<Genre>>> {
		return withContext(Dispatchers.IO) {
			fetchCacheThenNetworkResponse(
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
			)
		}
	}

	override fun getGenreDetail(
		genreId: Int,
	) = createPager { page ->
		apiRunCatching {
			flickSlateService.getGenreMovie(with_genres = genreId, page = page)
		}.map { Pair(it.toMoviesResponse().movies, it.total_pages ?: 0) }
	}.flow

}
