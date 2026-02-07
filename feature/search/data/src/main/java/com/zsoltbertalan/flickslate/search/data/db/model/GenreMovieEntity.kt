package com.zsoltbertalan.flickslate.search.data.db.model

import androidx.room.Entity
import com.zsoltbertalan.flickslate.shared.domain.model.Movie

@Entity(tableName = "genreMovies", primaryKeys = ["id", "genreId"])
data class GenreMovieEntity(
	val id: Int = 0,
	val genreId: Int = 0,
	val title: String = "",
	val overview: String? = null,
	val voteAverage: Float? = null,
	val posterPath: String? = null,
	val backdropPath: String? = null,
	val page: Int = 0,
)

internal fun Movie.toGenreMoviesEntity(genreId: Int, page: Int): GenreMovieEntity = GenreMovieEntity(
	id = id,
	genreId = genreId,
	title = this.title,
	overview = this.overview,
	voteAverage = this.voteAverage,
	posterPath = this.posterPath,
	backdropPath = this.backdropPath,
	page = page,
)

internal fun GenreMovieEntity.toMovie(): Movie = Movie(
	id = this.id,
	title = this.title,
	overview = this.overview,
	voteAverage = this.voteAverage,
	posterPath = this.posterPath,
	backdropPath = this.backdropPath,
)
