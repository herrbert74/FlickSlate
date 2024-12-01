package com.zsoltbertalan.flickslate.tv.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zsoltbertalan.flickslate.movies.data.db.model.NowPlayingMovieEntity
import com.zsoltbertalan.flickslate.movies.data.db.model.NowPlayingMoviesPageEntity
import com.zsoltbertalan.flickslate.movies.data.db.model.PopularMovieEntity
import com.zsoltbertalan.flickslate.movies.data.db.model.PopularMoviesPageEntity
import com.zsoltbertalan.flickslate.movies.data.db.model.UpcomingMovieEntity
import com.zsoltbertalan.flickslate.movies.data.db.model.UpcomingMoviesPageEntity
import com.zsoltbertalan.flickslate.tv.data.db.TvDao
import com.zsoltbertalan.flickslate.tv.data.db.model.TvPageEntity
import com.zsoltbertalan.flickslate.tv.data.db.model.TvShowEntity

@Database(
	entities = [
		TvShowEntity::class,
		TvPageEntity::class,
	],
	version = 1
)
abstract class TvDatabase : RoomDatabase() {

	abstract fun tvShowsDao(): TvDao
	abstract fun tvShowsPageDao(): TvPageDao

}
