package com.zsoltbertalan.flickslate.search.domain.api

import com.zsoltbertalan.flickslate.shared.util.Outcome
import com.zsoltbertalan.flickslate.domain.model.Genre
import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.domain.model.PagingReply
import kotlinx.coroutines.flow.Flow

interface GenreRepository {

	fun getGenresList(): Flow<Outcome<List<Genre>>>
	fun getGenreDetail(genreId: Int, page: Int): Flow<Outcome<PagingReply<Movie>>>

}
