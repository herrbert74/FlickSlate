package com.zsoltbertalan.flickslate.data.db

import com.zsoltbertalan.flickslate.domain.model.Tv
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class TvDbo() : RealmObject {

	constructor(
		id: Int = 0,
		name: String = "",
		overview: String? = null,
		voteAverage: Float? = null,
		posterPath: String? = null,
		backdropPath: String? = null,
		page: Int = 0,
	) : this() {
		this.id = id
		this.name = name
		this.overview = overview
		this.voteAverage = voteAverage
		this.posterPath = posterPath
		this.backdropPath = backdropPath
		this.page = page
	}

	@PrimaryKey
	var id: Int = 0
	var name: String = ""
	var overview: String? = null
	var voteAverage: Float? = null
	var posterPath: String? = null
	var backdropPath: String? = null
	var page: Int = 0

}

fun Tv.toTvDbo(page:Int): TvDbo = TvDbo(
	id = this.id,
	name = this.name,
	overview = this.overview,
	voteAverage = this.voteAverage,
	posterPath = this.posterPath,
	backdropPath = this.backdropPath,
	page = page,
)

fun TvDbo.toMovie(): Tv = Tv(
	id = this.id,
	name = this.name,
	overview = this.overview,
	voteAverage = this.voteAverage,
	posterPath = this.posterPath,
	backdropPath = this.backdropPath,
)
