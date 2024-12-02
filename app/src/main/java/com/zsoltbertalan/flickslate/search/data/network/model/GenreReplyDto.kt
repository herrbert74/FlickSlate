package com.zsoltbertalan.flickslate.search.data.network.model

import com.zsoltbertalan.flickslate.shared.data.network.model.GenreDto
import com.zsoltbertalan.flickslate.shared.data.network.model.toGenre
import com.zsoltbertalan.flickslate.shared.data.network.model.toGenresReply
import com.zsoltbertalan.flickslate.shared.model.Genre
import com.zsoltbertalan.flickslate.shared.model.GenresReply
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
