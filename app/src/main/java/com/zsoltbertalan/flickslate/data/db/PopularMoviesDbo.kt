package com.zsoltbertalan.flickslate.data.db

import com.zsoltbertalan.flickslate.domain.model.Movie
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class PopularMoviesDbo() : RealmObject {

	constructor(
		id: Int = 0,
		title: String = "",
		overview: String? = null,
		voteAverage: Float? = null,
		posterPath: String? = null,
		backdropPath: String? = null,
		page: Int = 0,
	) : this() {
		this.id = id
		this.title = title
		this.overview = overview
		this.voteAverage = voteAverage
		this.posterPath = posterPath
		this.backdropPath = backdropPath
		this.page = page
	}

	@PrimaryKey
	var id: Int = 0
	var title: String = ""
	var overview: String? = null
	var voteAverage: Float? = null
	var posterPath: String? = null
	var backdropPath: String? = null
	var page: Int = 0

}

fun Movie.toPopularMoviesDbo(page:Int): PopularMoviesDbo = PopularMoviesDbo(
	id = this.id,
	title = this.title,
	overview = this.overview,
	voteAverage = this.voteAverage,
	posterPath = this.posterPath,
	backdropPath = this.backdropPath,
	page = page,
)

fun PopularMoviesDbo.toMovie(): Movie = Movie(
	id = this.id,
	title = this.title,
	overview = this.overview,
	voteAverage = this.voteAverage,
	posterPath = this.posterPath,
	backdropPath = this.backdropPath,
)