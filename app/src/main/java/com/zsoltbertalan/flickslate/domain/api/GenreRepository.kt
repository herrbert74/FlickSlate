package com.zsoltbertalan.flickslate.domain.api

import androidx.paging.PagingData
import com.zsoltbertalan.flickslate.domain.model.Genre
import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.ext.Outcome
import kotlinx.coroutines.flow.Flow

interface GenreRepository {

	fun getGenresList(): Flow<Outcome<List<Genre>>>
	fun getGenreDetail(genreId: Int): Flow<PagingData<Movie>>

}
