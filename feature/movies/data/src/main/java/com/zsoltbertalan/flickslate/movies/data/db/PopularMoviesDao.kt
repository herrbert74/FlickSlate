package com.zsoltbertalan.flickslate.movies.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.zsoltbertalan.flickslate.movies.data.db.model.PopularMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface PopularMoviesDao {

	@Query("SELECT * FROM popularMovies")
	fun getAll(): List<PopularMovieEntity>

	@Query("SELECT * FROM popularMovies WHERE page LIKE :page")
	fun getPopularMovies(page: Int): Flow<List<PopularMovieEntity>>

	@Upsert
	fun insertPopularMovies(popularMovies: List<PopularMovieEntity>)

	@Query("DELETE FROM popularMovies")
	fun purgeDatabase()

}
