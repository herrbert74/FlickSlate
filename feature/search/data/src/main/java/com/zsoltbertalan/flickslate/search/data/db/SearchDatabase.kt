package com.zsoltbertalan.flickslate.search.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zsoltbertalan.flickslate.search.data.db.model.EtagEntity
import com.zsoltbertalan.flickslate.search.data.db.model.GenreEntity
import com.zsoltbertalan.flickslate.search.data.db.model.GenreMovieEntity
import com.zsoltbertalan.flickslate.search.data.db.model.GenreMoviesPageEntity

@Database(
	entities = [
		EtagEntity::class,
		GenreEntity::class,
		GenreMovieEntity::class,
		GenreMoviesPageEntity::class,
	],
	version = 1
)
internal abstract class SearchDatabase : RoomDatabase() {

	abstract fun etagDao(): ETagDao
	abstract fun genresDao(): GenresDao
	abstract fun genreMoviesDao(): GenreMoviesDao
	abstract fun genreMoviesPageDao(): GenreMoviesPageDao

}
