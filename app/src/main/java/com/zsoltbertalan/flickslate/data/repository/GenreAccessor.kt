package com.zsoltbertalan.flickslate.data.repository

import com.github.michaelbull.result.map
import com.zsoltbertalan.flickslate.data.network.FlickSlateService
import com.zsoltbertalan.flickslate.data.network.dto.toGenres
import com.zsoltbertalan.flickslate.data.network.dto.toMoviesResponse
import com.zsoltbertalan.flickslate.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.domain.model.Genre
import com.zsoltbertalan.flickslate.ext.ApiResult
import com.zsoltbertalan.flickslate.ext.apiRunCatching
import javax.inject.Singleton

@Singleton
class GenreAccessor(private val flickSlateService: FlickSlateService) : GenreRepository {

	override suspend fun getGenresList(): ApiResult<List<Genre>> {
		return apiRunCatching {
			flickSlateService.getGenres(language = "en").toGenres()
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
