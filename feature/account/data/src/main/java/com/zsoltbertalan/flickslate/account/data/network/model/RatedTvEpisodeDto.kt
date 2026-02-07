package com.zsoltbertalan.flickslate.account.data.network.model

import com.zsoltbertalan.flickslate.account.domain.model.RatedTvEpisode
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import kotlinx.serialization.Serializable

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class RatedTvEpisodeDto(
	val id: Int? = null,
	val show_id: Int? = null,
	val air_date: String? = null,
	val episode_number: Int? = null,
	val name: String? = null,
	val overview: String? = null,
	val season_number: Int? = null,
	val still_path: String? = null,
	val vote_average: Float? = null,
	val vote_count: Int? = null,
	val rating: Float
)

internal fun RatedTvEpisodeDto.toRatedTvEpisode() = RatedTvEpisode(
	tvEpisodeDetail = TvEpisodeDetail(
		id = this.id ?: 0,
		showId = this.show_id ?: 0,
		airDate = this.air_date ?: "",
		episodeNumber = this.episode_number ?: 0,
		name = this.name ?: "",
		overview = this.overview ?: "",
		seasonNumber = this.season_number ?: 0,
		stillPath = this.still_path,
		voteAverage = this.vote_average ?: 0f,
		voteCount = this.vote_count ?: 0
	),
	rating = this.rating
)
