package com.zsoltbertalan.flickslate.search.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.zsoltbertalan.flickslate.search.data.db.model.GenreMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreMoviesDao {

	@Query("SELECT * FROM genreMovies")
	fun getAll(): List<GenreMovieEntity>

	@Query("SELECT * FROM genreMovies WHERE page LIKE :page AND genreId LIKE :genreId")
	fun getGenreMovies(genreId: Int, page: Int): Flow<List<GenreMovieEntity>>

	@Upsert
	fun insertGenreMovies(genreMovies: List<GenreMovieEntity>)

	@Query("DELETE FROM genreMovies")
	fun purgeDatabase()

}
