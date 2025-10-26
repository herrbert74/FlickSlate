package com.zsoltbertalan.flickslate.account.data.network.model

import com.zsoltbertalan.flickslate.account.domain.model.RatedTvShow
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import kotlinx.serialization.Serializable

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
internal data class RatedTvShowDto(
	val popularity: Float? = null,
	val vote_count: Int? = null,
	val first_air_date: String? = null,
	val poster_path: String? = null,
	val id: Int? = null,
	val backdrop_path: String? = null,
	val original_language: String? = null,
	val original_title: String? = null,
	val genre_ids: List<Int>? = null,
	val name: String? = null,
	val vote_average: Float? = null,
	val overview: String? = null,
	val rating: Float
)

internal fun RatedTvShowDto.toRatedTvShow() = RatedTvShow(
		tvShow = TvShow(
			id = this.id ?: 0,
			name = this.name ?: "",
			overview = this.overview,
			voteAverage = this.vote_average,
			posterPath = this.poster_path,
			backdropPath = this.backdrop_path
		),
		rating = this.rating
)
