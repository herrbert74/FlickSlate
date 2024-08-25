package com.zsoltbertalan.flickslate.movies.data.db.model

import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class UpcomingMovieDbo() : RealmObject {

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

fun Movie.toUpcomingMoviesDbo(page:Int): UpcomingMovieDbo = UpcomingMovieDbo(
	id = this.id,
	title = this.title,
	overview = this.overview,
	voteAverage = this.voteAverage,
	posterPath = this.posterPath,
	backdropPath = this.backdropPath,
	page = page,
)

fun UpcomingMovieDbo.toMovie(): Movie = Movie(
	id = this.id,
	title = this.title,
	overview = this.overview,
	voteAverage = this.voteAverage,
	posterPath = this.posterPath,
	backdropPath = this.backdropPath,
)
