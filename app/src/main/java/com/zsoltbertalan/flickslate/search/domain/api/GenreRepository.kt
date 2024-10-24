package com.zsoltbertalan.flickslate.search.domain.api

import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.GenresReply
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import kotlinx.coroutines.flow.Flow

interface GenreRepository {

	fun getGenresList(): Flow<Outcome<GenresReply>>
	fun getGenreDetail(genreId: Int, page: Int): Flow<Outcome<PagingReply<Movie>>>

}
