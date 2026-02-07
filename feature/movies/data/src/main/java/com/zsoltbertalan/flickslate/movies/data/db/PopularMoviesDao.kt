package com.zsoltbertalan.flickslate.movies.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.zsoltbertalan.flickslate.movies.data.db.model.PopularMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PopularMoviesDao {

	@Query("SELECT * FROM popularMovies")
	fun getAll(): List<PopularMovieEntity>

	@Query("SELECT * FROM popularMovies WHERE page = :page ORDER BY popularity DESC")
	fun getPopularMovies(page: Int): Flow<List<PopularMovieEntity>>

	@Upsert
	fun insertPopularMovies(popularMovies: List<PopularMovieEntity>)

	@Query("DELETE FROM popularMovies WHERE page = :page AND id NOT IN (:currentMovieIds)")
	fun deleteNotOnlyInPage(page: Int, currentMovieIds: List<Int>)

	@Transaction
	fun upsertPopularMoviesTransaction(popularMovies: List<PopularMovieEntity>, page: Int) {
		insertPopularMovies(popularMovies)
		deleteNotOnlyInPage(page, popularMovies.map { it.id })
	}

	@Query("DELETE FROM popularMovies")
	fun purgeDatabase()

}
