package com.zsoltbertalan.flickslate.movies.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zsoltbertalan.flickslate.shared.model.Movie

@Entity(tableName = "nowPlayingMovies")
data class NowPlayingMovieEntity(
	@PrimaryKey val id: Int = 0,
	val title: String = "",
	val overview: String? = null,
	val voteAverage: Float? = null,
	val posterPath: String? = null,
	val backdropPath: String? = null,
	val page: Int = 0,
)

fun Movie.toNowPlayingMoviesEntity(page: Int): NowPlayingMovieEntity = NowPlayingMovieEntity(
	id = this.id,
	title = this.title,
	overview = this.overview,
	voteAverage = this.voteAverage,
	posterPath = this.posterPath,
	backdropPath = this.backdropPath,
	page = page,
)

fun NowPlayingMovieEntity.toMovie(): Movie = Movie(
	id = this.id,
	title = this.title,
	overview = this.overview,
	voteAverage = this.voteAverage,
	posterPath = this.posterPath,
	backdropPath = this.backdropPath,
)
