package com.zsoltbertalan.flickslate.domain.api

import com.zsoltbertalan.flickslate.common.util.Outcome
import com.zsoltbertalan.flickslate.domain.model.Genre
import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.domain.model.PagingReply
import kotlinx.coroutines.flow.Flow

interface GenreRepository {

	fun getGenresList(): Flow<Outcome<List<Genre>>>
	fun getGenreDetail(genreId: Int, page: Int): Flow<Outcome<PagingReply<Movie>>>

}
