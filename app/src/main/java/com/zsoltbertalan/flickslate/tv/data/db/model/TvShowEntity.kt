package com.zsoltbertalan.flickslate.tv.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zsoltbertalan.flickslate.tv.domain.model.TvShow

@Entity(tableName = "TvShows")
data class TvShowEntity(

	@PrimaryKey
	val id: Int = 0,

	val name: String = "",
	val overview: String? = null,
	val voteAverage: Float? = null,
	val posterPath: String? = null,
	val backdropPath: String? = null,
	val page: Int = 0,
)

fun TvShow.toTvEntity(page: Int): TvShowEntity = TvShowEntity(
	id = this.id,
	name = this.name,
	overview = this.overview,
	voteAverage = this.voteAverage,
	posterPath = this.posterPath,
	backdropPath = this.backdropPath,
	page = page,
)

fun TvShowEntity.toTvShow(): TvShow = TvShow(
	id = this.id,
	name = this.name,
	overview = this.overview,
	voteAverage = this.voteAverage,
	posterPath = this.posterPath,
	backdropPath = this.backdropPath,
)
