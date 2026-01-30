package com.zsoltbertalan.flickslate.movies.data.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.zsoltbertalan.flickslate.movies.data.db.model.NowPlayingMovieEntity
import com.zsoltbertalan.flickslate.movies.data.db.model.PopularMovieEntity
import com.zsoltbertalan.flickslate.movies.data.db.model.UpcomingMovieEntity
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class MoviesDatabaseTest {

	private lateinit var db: MoviesDatabase
	private lateinit var popularDao: PopularMoviesDao
	private lateinit var nowPlayingDao: NowPlayingMoviesDao
	private lateinit var upcomingDao: UpcomingMoviesDao

	@Before
	fun createDb() {
		val context = ApplicationProvider.getApplicationContext<android.content.Context>()
		db = Room.inMemoryDatabaseBuilder(context, MoviesDatabase::class.java)
			.allowMainThreadQueries()
			.build()
		popularDao = db.popularMoviesDao()
		nowPlayingDao = db.nowPlayingMoviesDao()
		upcomingDao = db.upcomingMoviesDao()
	}

	@After
	@Throws(IOException::class)
	fun closeDb() {
		db.close()
	}

	@Test
	fun testPopularityOrdering() = runTest {
		val movie1 = PopularMovieEntity(id = 1, title = "Movie 1", popularity = 10.0, page = 1)
		val movie2 = PopularMovieEntity(id = 2, title = "Movie 2", popularity = 20.0, page = 1)
		val movie3 = PopularMovieEntity(id = 3, title = "Movie 3", popularity = 5.0, page = 1)

		popularDao.insertPopularMovies(listOf(movie1, movie2, movie3))

		val result = popularDao.getPopularMovies(1).first()

		result.size shouldBe 3
		// Ordered by popularity DESC
		result[0].id shouldBe 2 // 20.0
		result[1].id shouldBe 1 // 10.0
		result[2].id shouldBe 3 // 5.0
	}

	@Test
	fun testAtomicUpsertDeletesOldItems() = runTest {
		val movie1 = PopularMovieEntity(id = 1, title = "Movie 1", popularity = 10.0, page = 1)
		val movie2 = PopularMovieEntity(id = 2, title = "Movie 2", popularity = 20.0, page = 1)

		popularDao.insertPopularMovies(listOf(movie1, movie2))

		// New list for page 1: Movie 2 stays, Movie 1 is gone, Movie 3 is added
		val movie3 = PopularMovieEntity(id = 3, title = "Movie 3", popularity = 30.0, page = 1)
		val movie2Updated = movie2.copy(popularity = 25.0)
		val newUpdate = listOf(movie2Updated, movie3)

		popularDao.upsertPopularMoviesTransaction(newUpdate, 1)

		val result = popularDao.getPopularMovies(1).first()

		result.size shouldBe 2
		// Ordered by popularity: Movie 3 (30.0), Movie 2 (25.0)
		result[0].id shouldBe 3
		result[1].id shouldBe 2

		// Ensure movie 1 is gone
		val allMovies = popularDao.getAll()
		val movie1Found = allMovies.find { it.id == 1 }
		movie1Found shouldBe null
	}

	@Test
	fun testNowPlayingPopularityOrdering() = runTest {
		val movie1 = NowPlayingMovieEntity(id = 1, title = "Now Playing 1", popularity = 15.0, page = 1)
		val movie2 = NowPlayingMovieEntity(id = 2, title = "Now Playing 2", popularity = 25.0, page = 1)
		val movie3 = NowPlayingMovieEntity(id = 3, title = "Now Playing 3", popularity = 8.0, page = 1)

		nowPlayingDao.insertNowPlayingMovies(listOf(movie1, movie2, movie3))

		val result = nowPlayingDao.getNowPlayingMovies(1).first()

		result.size shouldBe 3
		// Ordered by popularity DESC
		result[0].id shouldBe 2 // 25.0
		result[1].id shouldBe 1 // 15.0
		result[2].id shouldBe 3 // 8.0
	}

	@Test
	fun testNowPlayingAtomicUpsertDeletesOldItems() = runTest {
		val movie1 = NowPlayingMovieEntity(id = 1, title = "Now Playing 1", popularity = 15.0, page = 1)
		val movie2 = NowPlayingMovieEntity(id = 2, title = "Now Playing 2", popularity = 25.0, page = 1)

		nowPlayingDao.insertNowPlayingMovies(listOf(movie1, movie2))

		// New list for page 1: Movie 2 stays, Movie 1 is gone, Movie 3 is added
		val movie3 = NowPlayingMovieEntity(id = 3, title = "Now Playing 3", popularity = 35.0, page = 1)
		val movie2Updated = movie2.copy(popularity = 28.0)
		val newUpdate = listOf(movie2Updated, movie3)

		nowPlayingDao.upsertNowPlayingMoviesTransaction(newUpdate, 1)

		val result = nowPlayingDao.getNowPlayingMovies(1).first()

		result.size shouldBe 2
		// Ordered by popularity: Movie 3 (35.0), Movie 2 (28.0)
		result[0].id shouldBe 3
		result[1].id shouldBe 2

		// Ensure movie 1 is gone
		val allMovies = nowPlayingDao.getAll()
		val movie1Found = allMovies.find { it.id == 1 }
		movie1Found shouldBe null
	}

	@Test
	fun testUpcomingPopularityOrdering() = runTest {
		val movie1 = UpcomingMovieEntity(id = 1, title = "Upcoming 1", popularity = 12.0, page = 1)
		val movie2 = UpcomingMovieEntity(id = 2, title = "Upcoming 2", popularity = 22.0, page = 1)
		val movie3 = UpcomingMovieEntity(id = 3, title = "Upcoming 3", popularity = 7.0, page = 1)

		upcomingDao.insertUpcomingMovies(listOf(movie1, movie2, movie3))

		val result = upcomingDao.getUpcomingMovies(1).first()

		result.size shouldBe 3
		// Ordered by popularity DESC
		result[0].id shouldBe 2 // 22.0
		result[1].id shouldBe 1 // 12.0
		result[2].id shouldBe 3 // 7.0
	}

	@Test
	fun testUpcomingAtomicUpsertDeletesOldItems() = runTest {
		val movie1 = UpcomingMovieEntity(id = 1, title = "Upcoming 1", popularity = 12.0, page = 1)
		val movie2 = UpcomingMovieEntity(id = 2, title = "Upcoming 2", popularity = 22.0, page = 1)

		upcomingDao.insertUpcomingMovies(listOf(movie1, movie2))

		// New list for page 1: Movie 2 stays, Movie 1 is gone, Movie 3 is added
		val movie3 = UpcomingMovieEntity(id = 3, title = "Upcoming 3", popularity = 32.0, page = 1)
		val movie2Updated = movie2.copy(popularity = 27.0)
		val newUpdate = listOf(movie2Updated, movie3)

		upcomingDao.upsertUpcomingMoviesTransaction(newUpdate, 1)

		val result = upcomingDao.getUpcomingMovies(1).first()

		result.size shouldBe 2
		// Ordered by popularity: Movie 3 (32.0), Movie 2 (27.0)
		result[0].id shouldBe 3
		result[1].id shouldBe 2

		// Ensure movie 1 is gone
		val allMovies = upcomingDao.getAll()
		val movie1Found = allMovies.find { it.id == 1 }
		movie1Found shouldBe null
	}
}
