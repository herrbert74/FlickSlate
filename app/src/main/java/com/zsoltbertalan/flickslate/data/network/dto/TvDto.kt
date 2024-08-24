package com.zsoltbertalan.flickslate.data.network.dto

import com.babestudios.base.data.mapNullInputList
import com.zsoltbertalan.flickslate.domain.model.Tv
import kotlinx.serialization.Serializable

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class TvDto(
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
)

fun List<TvDto>.toTvList(): List<Tv> = mapNullInputList(this) { tvDto -> tvDto.toTv() }

fun TvDto.toTv() = Tv(
	this.id ?: 0,
	this.name ?: "",
	this.overview,
	this.vote_average,
	this.poster_path,
	this.backdrop_path
)
