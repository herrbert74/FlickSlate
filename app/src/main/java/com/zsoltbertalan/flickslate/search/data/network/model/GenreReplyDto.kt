package com.zsoltbertalan.flickslate.search.data.network.model

import com.babestudios.base.data.mapNullInputList
import com.zsoltbertalan.flickslate.movies.data.network.model.toMoviesReply
import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import com.zsoltbertalan.flickslate.shared.domain.model.GenresReply
import kotlinx.serialization.Serializable
import retrofit2.Response

@Serializable
data class GenreReplyDto(
	val genres: List<GenreDto> = emptyList(),
)

fun GenreReplyDto.toGenres(): List<Genre> = this.genres.toGenresReply()

fun Response<GenreReplyDto>.toGenresReply(): GenresReply {
	val body = this.body()!!
	val genres = body.genres
	val etag = this.headers()["etag"] ?: ""
	return GenresReply(genres.map { it.toGenre() }, etag)
}
