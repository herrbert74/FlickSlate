package com.zsoltbertalan.flickslate.movies.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.zsoltbertalan.flickslate.movies.data.db.model.UpcomingMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface UpcomingMoviesDao {

	@Query("SELECT * FROM upcomingMovies")
	fun getAll(): List<UpcomingMovieEntity>

	@Query("SELECT * FROM upcomingMovies WHERE page LIKE :page")
	fun getUpcomingMovies(page: Int): Flow<List<UpcomingMovieEntity>>

	@Upsert
	fun insertUpcomingMovies(upcomingMovies: List<UpcomingMovieEntity>)

	@Query("DELETE FROM upcomingMovies")
	fun purgeDatabase()

}
