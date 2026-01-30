package com.zsoltbertalan.flickslate.movies.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.zsoltbertalan.flickslate.movies.data.db.model.NowPlayingMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface NowPlayingMoviesDao {

	@Query("SELECT * FROM nowPlayingMovies")
	fun getAll(): List<NowPlayingMovieEntity>

	@Query("SELECT * FROM nowPlayingMovies WHERE page = :page ORDER BY popularity DESC")
	fun getNowPlayingMovies(page: Int): Flow<List<NowPlayingMovieEntity>>

	@Upsert
	fun insertNowPlayingMovies(nowPlayingMovies: List<NowPlayingMovieEntity>)

	@Transaction
	fun upsertNowPlayingMoviesTransaction(nowPlayingMovies: List<NowPlayingMovieEntity>, page: Int) {
		insertNowPlayingMovies(nowPlayingMovies)
		deleteNotOnlyInPage(page, nowPlayingMovies.map { it.id })
	}

	@Query("DELETE FROM nowPlayingMovies WHERE page = :page AND id NOT IN (:ids)")
	fun deleteNotOnlyInPage(page: Int, ids: List<Int>)

	@Query("DELETE FROM nowPlayingMovies")
	fun purgeDatabase()

}
