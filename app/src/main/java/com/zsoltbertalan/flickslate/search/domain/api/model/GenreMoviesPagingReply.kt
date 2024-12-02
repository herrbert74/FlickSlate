package com.zsoltbertalan.flickslate.search.domain.api.model

import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.model.PagingReply

/**
 * A [PagingReply] of [Movie]s for a Genre id.
 */
data class GenreMoviesPagingReply(val genreId: Int, val pagingReply: PagingReply<Movie>)
