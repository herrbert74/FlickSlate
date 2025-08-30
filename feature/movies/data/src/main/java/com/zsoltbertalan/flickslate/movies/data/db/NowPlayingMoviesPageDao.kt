package com.zsoltbertalan.flickslate.movies.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zsoltbertalan.flickslate.movies.data.db.model.NowPlayingMoviesPageEntity

@Dao
internal interface NowPlayingMoviesPageDao {

	@Query("SELECT * FROM nowPlayingMoviesPages WHERE page LIKE :page")
	fun getPageData(page: Int): List<NowPlayingMoviesPageEntity>

	@Insert
	fun insertPageData(nowPlayingMoviesPage: NowPlayingMoviesPageEntity)

}
