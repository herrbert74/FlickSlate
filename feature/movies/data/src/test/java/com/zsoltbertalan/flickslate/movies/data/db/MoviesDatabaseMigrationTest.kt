package com.zsoltbertalan.flickslate.movies.data.db

import androidx.room.testing.MigrationTestHelper
import androidx.test.platform.app.InstrumentationRegistry
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MoviesDatabaseMigrationTest {

	private val testDb = "migration-test"

	@get:Rule
	val helper: MigrationTestHelper = MigrationTestHelper(
		InstrumentationRegistry.getInstrumentation(),
		MoviesDatabase::class.java
	)

	@Test
	fun migrate1To2() {
		helper.createDatabase(testDb, 1).apply {
			// db has schema version 1.
			// insert some data using SQL matching version 1 schema (PopularMovieEntity before change)
			// page was present in version 1.
			execSQL("INSERT INTO popularMovies (id, title, page) VALUES (1, 'Movie 1', 1)")
			close()
		}

		// Re-open the database with version 2 and provide MIGRATION_1_2
		val db = helper.runMigrationsAndValidate(testDb, 2, true, MIGRATION_1_2)

		// Query to check if columns were added
		val cursor = db.query("SELECT * FROM popularMovies WHERE id = 1")
		cursor.moveToFirst() shouldBe true
		val popularityIndex = cursor.getColumnIndex("popularity")
		val releaseDateIndex = cursor.getColumnIndex("releaseDate")

		popularityIndex shouldNotBe -1
		releaseDateIndex shouldNotBe -1
	}
}
