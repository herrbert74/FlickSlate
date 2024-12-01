package com.zsoltbertalan.flickslate.movies.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zsoltbertalan.flickslate.movies.data.db.model.PopularMoviesPageEntity

@Dao
interface PopularMoviesPageDao {

	@Query("SELECT * FROM popularMoviesPages WHERE page LIKE :page")
	fun getPageData(page: Int): List<PopularMoviesPageEntity>

	@Insert
	fun insertPageData(popularMoviesPage: PopularMoviesPageEntity)

}
