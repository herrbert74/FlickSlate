package com.zsoltbertalan.flickslate.movies.data.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.zsoltbertalan.flickslate.movies.data.db.model.PopularMovieEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class MoviesDatabaseTest {

	private lateinit var db: MoviesDatabase
	private lateinit var dao: PopularMoviesDao

	@Before
	fun createDb() {
		val context = ApplicationProvider.getApplicationContext<android.content.Context>()
		db = Room.inMemoryDatabaseBuilder(context, MoviesDatabase::class.java)
			.allowMainThreadQueries()
			.build()
		dao = db.popularMoviesDao()
	}

	@After
	@Throws(IOException::class)
	fun closeDb() {
		db.close()
	}

	@Test
	fun testPopularityOrdering() = runBlocking {
		val movie1 = PopularMovieEntity(id = 1, title = "Movie 1", popularity = 10.0, page = 1)
		val movie2 = PopularMovieEntity(id = 2, title = "Movie 2", popularity = 20.0, page = 1)
		val movie3 = PopularMovieEntity(id = 3, title = "Movie 3", popularity = 5.0, page = 1)

		dao.insertPopularMovies(listOf(movie1, movie2, movie3))

		val result = dao.getPopularMovies(1).first()

		assertEquals(3, result.size)
		// Ordered by popularity DESC
		assertEquals(2, result[0].id) // 20.0
		assertEquals(1, result[1].id) // 10.0
		assertEquals(3, result[2].id) // 5.0
	}

	@Test
	fun testAtomicUpsertDeletesOldItems() = runBlocking {
		val movie1 = PopularMovieEntity(id = 1, title = "Movie 1", popularity = 10.0, page = 1)
		val movie2 = PopularMovieEntity(id = 2, title = "Movie 2", popularity = 20.0, page = 1)

		dao.insertPopularMovies(listOf(movie1, movie2))

		// New list for page 1: Movie 2 stays, Movie 1 is gone, Movie 3 is added
		val movie3 = PopularMovieEntity(id = 3, title = "Movie 3", popularity = 30.0, page = 1)
		val movie2Updated = movie2.copy(popularity = 25.0)
		val newUpdate = listOf(movie2Updated, movie3)

		dao.upsertPopularMoviesTransaction(newUpdate, 1)

		val result = dao.getPopularMovies(1).first()

		assertEquals(2, result.size)
		// Ordered by popularity: Movie 3 (30.0), Movie 2 (25.0)
		assertEquals(3, result[0].id)
		assertEquals(2, result[1].id)

		// Ensure movie 1 is gone
		val allMovies = dao.getAll()
		val movie1Found = allMovies.find { it.id == 1 }
		assertNull(movie1Found)
	}
}
