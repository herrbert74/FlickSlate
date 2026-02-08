package com.zsoltbertalan.flickslate.movies.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.zsoltbertalan.flickslate.movies.data.db.model.UpcomingMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UpcomingMoviesDao {

	@Query("SELECT * FROM upcomingMovies")
	fun getAll(): List<UpcomingMovieEntity>

	@Query("SELECT * FROM upcomingMovies WHERE page = :page ORDER BY popularity DESC")
	fun getUpcomingMovies(page: Int): Flow<List<UpcomingMovieEntity>>

	@Upsert
	fun insertUpcomingMovies(upcomingMovies: List<UpcomingMovieEntity>)

	@Transaction
	fun upsertUpcomingMoviesTransaction(upcomingMovies: List<UpcomingMovieEntity>, page: Int) {
		insertUpcomingMovies(upcomingMovies)
		deleteNotOnlyInPage(page, upcomingMovies.map { it.id })
	}

	@Query("DELETE FROM upcomingMovies WHERE page = :page AND id NOT IN (:ids)")
	fun deleteNotOnlyInPage(page: Int, ids: List<Int>)

	@Query("DELETE FROM upcomingMovies")
	fun purgeDatabase()

}
