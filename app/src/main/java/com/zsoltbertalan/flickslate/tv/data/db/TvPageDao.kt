package com.zsoltbertalan.flickslate.tv.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zsoltbertalan.flickslate.movies.data.db.model.PopularMoviesPageEntity
import com.zsoltbertalan.flickslate.tv.data.db.model.TvPageEntity

@Dao
interface TvPageDao {

	@Query("SELECT * FROM tvpages WHERE page LIKE :page")
	fun getPageData(page: Int): List<TvPageEntity>

	@Insert
	fun insertPageData(tvPage: TvPageEntity)

}
