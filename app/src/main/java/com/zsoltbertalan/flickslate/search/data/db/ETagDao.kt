package com.zsoltbertalan.flickslate.search.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.zsoltbertalan.flickslate.movies.data.db.model.PopularMovieEntity
import com.zsoltbertalan.flickslate.search.data.db.model.EtagEntity
import com.zsoltbertalan.flickslate.search.data.db.model.GenreMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ETagDao {

	@Query("SELECT * FROM etags")
	fun getAll(): List<EtagEntity>

	@Upsert
	fun insertEtag(eTag: EtagEntity)

	@Query("DELETE FROM etags")
	fun purgeDatabase()

}
