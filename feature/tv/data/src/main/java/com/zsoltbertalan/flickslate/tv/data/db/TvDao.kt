package com.zsoltbertalan.flickslate.tv.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.zsoltbertalan.flickslate.tv.data.db.model.TvShowEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TvDao {

	@Query("SELECT * FROM tvshows")
	fun getAll(): List<TvShowEntity>

	@Query("SELECT * FROM tvshows WHERE page LIKE :page")
	fun getTvShows(page: Int): Flow<List<TvShowEntity>>

	@Upsert
	fun insertTvShows(tvShows: List<TvShowEntity>)

	@Query("DELETE FROM tvshows")
	fun purgeDatabase()

}
