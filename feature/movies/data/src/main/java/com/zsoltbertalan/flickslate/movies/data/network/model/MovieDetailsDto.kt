package com.zsoltbertalan.flickslate.movies.data.network.model

import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetail
import com.zsoltbertalan.flickslate.shared.data.network.model.GenreDto
import com.zsoltbertalan.flickslate.shared.data.network.model.toGenre
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class MovieDetailsDto(
	val id: Int? = null,
	val title: String? = null,
	val overview: String? = null,
	val vote_average: Float? = null,
	val poster_path: String? = null,
	val backdrop_path: String? = null,
	val genres: List<GenreDto>? = null,
	val account_states: AccountStatesDto? = null,
)

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class AccountStatesDto(
	val rated: JsonElement? = null,
	val favorite: Boolean = false,
	val watchlist: Boolean = false,
)

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class RatedDto(
	val value: Float? = null,
)

internal fun MovieDetailsDto.toMovieDetail(): MovieDetail {
	val ratedDto = this.account_states?.rated?.let {
		if (it is JsonObject) {
			Json.decodeFromJsonElement(RatedDto.serializer(), it)
		} else {
			null
		}
	}

	return MovieDetail(
		id = this.id,
		title = this.title,
		overview = this.overview,
		voteAverage = this.vote_average,
		posterPath = this.poster_path,
		backdropPath = this.backdrop_path,
		genres = this.genres?.map { it.toGenre() }?.toImmutableList()
			?: emptyList<com.zsoltbertalan.flickslate.shared.domain.model.Genre>().toImmutableList(),
		personalRating = ratedDto?.value ?: -1f,
		favorite = this.account_states?.favorite ?: false,
		watchlist = this.account_states?.watchlist ?: false,
	)
}
