package com.zsoltbertalan.flickslate.movies.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
	version = 2
)
abstract class MoviesDatabase : RoomDatabase() {

	abstract fun popularMoviesDao(): PopularMoviesDao
	abstract fun popularMoviesPageDao(): PopularMoviesPageDao
	abstract fun nowPlayingMoviesDao(): NowPlayingMoviesDao
	abstract fun nowPlayingMoviesPageDao(): NowPlayingMoviesPageDao
	abstract fun upcomingMoviesDao(): UpcomingMoviesDao
	abstract fun upcomingMoviesPageDao(): UpcomingMoviesPageDao

}

internal val MIGRATION_1_2 = object : Migration(1, 2) {
	override fun migrate(db: SupportSQLiteDatabase) {
		db.execSQL("ALTER TABLE popularMovies ADD COLUMN popularity REAL")
		db.execSQL("ALTER TABLE popularMovies ADD COLUMN releaseDate TEXT")
		db.execSQL("ALTER TABLE nowPlayingMovies ADD COLUMN popularity REAL")
		db.execSQL("ALTER TABLE nowPlayingMovies ADD COLUMN releaseDate TEXT")
		db.execSQL("ALTER TABLE upcomingMovies ADD COLUMN popularity REAL")
		db.execSQL("ALTER TABLE upcomingMovies ADD COLUMN releaseDate TEXT")
	}
}
