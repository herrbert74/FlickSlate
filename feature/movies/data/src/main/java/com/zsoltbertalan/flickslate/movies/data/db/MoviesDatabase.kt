package com.zsoltbertalan.flickslate.movies.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zsoltbertalan.flickslate.movies.data.db.model.NowPlayingMovieEntity
import com.zsoltbertalan.flickslate.movies.data.db.model.NowPlayingMoviesPageEntity
import com.zsoltbertalan.flickslate.movies.data.db.model.PopularMovieEntity
import com.zsoltbertalan.flickslate.movies.data.db.model.PopularMoviesPageEntity
import com.zsoltbertalan.flickslate.movies.data.db.model.UpcomingMovieEntity
import com.zsoltbertalan.flickslate.movies.data.db.model.UpcomingMoviesPageEntity

@Database(
	entities = [
		PopularMovieEntity::class,
		PopularMoviesPageEntity::class,
		NowPlayingMovieEntity::class,
		NowPlayingMoviesPageEntity::class,
		UpcomingMovieEntity::class,
		UpcomingMoviesPageEntity::class,
	],
	version = 1
)
internal abstract class MoviesDatabase : RoomDatabase() {

	abstract fun popularMoviesDao(): PopularMoviesDao
	abstract fun popularMoviesPageDao(): PopularMoviesPageDao
	abstract fun nowPlayingMoviesDao(): NowPlayingMoviesDao
	abstract fun nowPlayingMoviesPageDao(): NowPlayingMoviesPageDao
	abstract fun upcomingMoviesDao(): UpcomingMoviesDao
	abstract fun upcomingMoviesPageDao(): UpcomingMoviesPageDao

}
