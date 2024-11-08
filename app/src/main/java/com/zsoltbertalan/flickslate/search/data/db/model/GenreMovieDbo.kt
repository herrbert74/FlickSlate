package com.zsoltbertalan.flickslate.search.data.db.model

import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class GenreMovieDbo() : RealmObject {

	constructor(
		idAndGenreId: String = "",
		title: String = "",
		overview: String? = null,
		voteAverage: Float? = null,
		posterPath: String? = null,
		backdropPath: String? = null,
		page: Int = 0,
	) : this() {
		this.idAndGenreId = idAndGenreId
		this.title = title
		this.overview = overview
		this.voteAverage = voteAverage
		this.posterPath = posterPath
		this.backdropPath = backdropPath
		this.page = page
	}

	@PrimaryKey
	var idAndGenreId: String = ""
	var title: String = ""
	var overview: String? = null
	var voteAverage: Float? = null
	var posterPath: String? = null
	var backdropPath: String? = null
	var page: Int = 0

}

fun Movie.toGenreMoviesDbo(genreId:Int, page:Int): GenreMovieDbo = GenreMovieDbo(
	idAndGenreId = "${id}_${genreId}",
	title = this.title,
	overview = this.overview,
	voteAverage = this.voteAverage,
	posterPath = this.posterPath,
	backdropPath = this.backdropPath,
	page = page,
)

fun GenreMovieDbo.toMovie(): Movie = Movie(
	id = this.idAndGenreId.split("_").getOrElse(0) { "0" }.toInt(),
	title = this.title,
	overview = this.overview,
	voteAverage = this.voteAverage,
	posterPath = this.posterPath,
	backdropPath = this.backdropPath,
)
