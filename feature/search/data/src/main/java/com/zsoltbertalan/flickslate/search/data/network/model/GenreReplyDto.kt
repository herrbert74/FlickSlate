package com.zsoltbertalan.flickslate.search.data.network.model

import com.zsoltbertalan.flickslate.shared.data.network.model.GenreDto
import com.zsoltbertalan.flickslate.shared.data.network.model.toGenre
import com.zsoltbertalan.flickslate.shared.data.network.model.toGenresReply
import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import com.zsoltbertalan.flickslate.shared.domain.model.GenresReply
import kotlinx.serialization.Serializable
import retrofit2.Response

@Serializable
internal data class GenreReplyDto(
	val genres: List<GenreDto> = emptyList(),
)

internal fun GenreReplyDto.toGenres(): List<Genre> = this.genres.toGenresReply()

internal fun Response<GenreReplyDto>.toGenresReply(): GenresReply {
	val body = this.body()!!
	val genres = body.genres
	val etag = this.headers()["etag"] ?: ""
	return GenresReply(genres.map { it.toGenre() }, etag)
}
