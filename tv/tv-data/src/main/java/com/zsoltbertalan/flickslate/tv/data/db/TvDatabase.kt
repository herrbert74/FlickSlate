package com.zsoltbertalan.flickslate.tv.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
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
