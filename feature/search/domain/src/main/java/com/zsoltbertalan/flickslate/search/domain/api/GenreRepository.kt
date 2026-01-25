package com.zsoltbertalan.flickslate.search.domain.api

import com.zsoltbertalan.flickslate.search.domain.api.model.GenreMoviesPagingReply
import com.zsoltbertalan.flickslate.shared.domain.model.GenresReply
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import kotlinx.coroutines.flow.Flow

interface GenreRepository {

	fun getGenresList(): Flow<Outcome<GenresReply>>
	fun getGenreDetail(genreId: Int, page: Int): Flow<Outcome<GenreMoviesPagingReply>>

}
