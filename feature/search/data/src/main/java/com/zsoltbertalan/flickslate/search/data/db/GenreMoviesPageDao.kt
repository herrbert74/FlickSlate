package com.zsoltbertalan.flickslate.search.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zsoltbertalan.flickslate.search.data.db.model.GenreMoviesPageEntity

@Dao
interface GenreMoviesPageDao {

	@Query("SELECT * FROM genremoviespages WHERE page LIKE :page")
	fun getPageData(page: Int): List<GenreMoviesPageEntity>

	@Insert
	fun insertPageData(genreMoviesPage: GenreMoviesPageEntity)

}
