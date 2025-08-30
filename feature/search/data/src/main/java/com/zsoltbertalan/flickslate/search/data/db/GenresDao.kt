package com.zsoltbertalan.flickslate.search.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.zsoltbertalan.flickslate.search.data.db.model.GenreEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface GenresDao {

	@Query("SELECT * FROM genres")
	fun getAll(): Flow<List<GenreEntity>>

	@Query("SELECT * FROM genres WHERE id LIKE :id")
	fun getGenre(id: Int): GenreEntity?

	@Upsert
	fun insertGenres(genres: List<GenreEntity>)

	@Query("DELETE FROM genres")
	fun purgeDatabase()

}
