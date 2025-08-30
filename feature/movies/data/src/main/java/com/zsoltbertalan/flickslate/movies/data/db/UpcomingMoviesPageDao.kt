package com.zsoltbertalan.flickslate.movies.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zsoltbertalan.flickslate.movies.data.db.model.UpcomingMoviesPageEntity

@Dao
internal interface UpcomingMoviesPageDao {

	@Query("SELECT * FROM upcomingmoviespages WHERE page LIKE :page")
	fun getPageData(page: Int): List<UpcomingMoviesPageEntity>

	@Insert
	fun insertPageData(upcomingMoviesPage: UpcomingMoviesPageEntity)

}
