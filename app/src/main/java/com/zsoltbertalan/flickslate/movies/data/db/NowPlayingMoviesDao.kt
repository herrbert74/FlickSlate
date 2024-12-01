package com.zsoltbertalan.flickslate.movies.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.zsoltbertalan.flickslate.movies.data.db.model.NowPlayingMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NowPlayingMoviesDao {

	@Query("SELECT * FROM nowPlayingMovies")
	fun getAll(): List<NowPlayingMovieEntity>

	@Query("SELECT * FROM nowPlayingMovies WHERE page LIKE :page")
	fun getNowPlayingMovies(page: Int): Flow<List<NowPlayingMovieEntity>>

	@Upsert
	fun insertNowPlayingMovies(nowPlayingMovies: List<NowPlayingMovieEntity>)

	@Query("DELETE FROM nowPlayingMovies")
	fun purgeDatabase()

}
