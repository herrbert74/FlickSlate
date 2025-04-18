package com.zsoltbertalan.flickslate.movies.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zsoltbertalan.flickslate.shared.model.Movie

@Entity(tableName = "upcomingMovies")
class UpcomingMovieEntity(
	@PrimaryKey
	val id: Int = 0,

	val title: String = "",
	val overview: String? = null,
	val voteAverage: Float? = null,
	val posterPath: String? = null,
	val backdropPath: String? = null,
	val page: Int = 0,
)

fun Movie.toUpcomingMoviesEntity(page: Int): UpcomingMovieEntity = UpcomingMovieEntity(
	id = this.id,
	title = this.title,
	overview = this.overview,
	voteAverage = this.voteAverage,
	posterPath = this.posterPath,
	backdropPath = this.backdropPath,
	page = page,
)

fun UpcomingMovieEntity.toMovie(): Movie = Movie(
	id = this.id,
	title = this.title,
	overview = this.overview,
	voteAverage = this.voteAverage,
	posterPath = this.posterPath,
	backdropPath = this.backdropPath,
)
