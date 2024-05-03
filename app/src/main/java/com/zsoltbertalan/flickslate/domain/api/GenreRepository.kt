package com.zsoltbertalan.flickslate.domain.api

import androidx.paging.PagingData
import com.zsoltbertalan.flickslate.domain.model.Genre
import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.ext.ApiResult
import kotlinx.coroutines.flow.Flow

interface GenreRepository {

	fun getGenresList(): Flow<ApiResult<List<Genre>>>
	fun getGenreDetail(genreId: Int): Flow<PagingData<Movie>>

}
