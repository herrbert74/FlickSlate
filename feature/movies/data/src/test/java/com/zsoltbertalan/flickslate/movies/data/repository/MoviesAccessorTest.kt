package com.zsoltbertalan.flickslate.movies.data.repository

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.movies.data.api.NowPlayingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.api.PopularMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.api.UpcomingMoviesDataSource
import com.zsoltbertalan.flickslate.movies.data.network.MoviesService
import com.zsoltbertalan.flickslate.movies.data.network.model.AccountStatesDto
import com.zsoltbertalan.flickslate.movies.data.network.model.MovieDtoMother
import com.zsoltbertalan.flickslate.movies.data.network.model.RatedDto
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetail
import com.zsoltbertalan.flickslate.shared.domain.model.MovieMother
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class MoviesAccessorTest {

	private val moviesService: MoviesService = mockk()

	private val popularMoviesDataSource: PopularMoviesDataSource.Local = mockk()
	private val upcomingMoviesDataSource: UpcomingMoviesDataSource.Local = mockk()
	private val nowPlayingMoviesDataSource: NowPlayingMoviesDataSource.Local = mockk()
	private val popularMoviesRemoteDataSource: PopularMoviesDataSource.Remote = mockk()
	private val upcomingMoviesRemoteDataSource: UpcomingMoviesDataSource.Remote = mockk()
	private val nowPlayingMoviesRemoteDataSource: NowPlayingMoviesDataSource.Remote = mockk()

	private lateinit var moviesAccessor: MoviesAccessor

	@Before
	fun setup() {
		coEvery {
			moviesService.getPopularMovies(any(), any(), any())
		} returns Response.success(MovieDtoMother.createPopularMovieList())
		coEvery {
			moviesService.getNowPlayingMovies(any(), any(), any())
		} returns Response.success(MovieDtoMother.createNowPlayingMovieList())
		coEvery {
			moviesService.getUpcomingMovies(any(), any(), any())
		} returns Response.success(MovieDtoMother.createUpcomingMovieList())
		coEvery { popularMoviesDataSource.getEtag(any()) } returns null
		coEvery { popularMoviesDataSource.insertPopularMovies(any(), any()) } returns Unit
		coEvery { popularMoviesDataSource.insertPopularMoviesPageData(any()) } returns Unit
		coEvery { popularMoviesDataSource.getPopularMovies(any()) } returns flowOf(null)
		coEvery { upcomingMoviesDataSource.getEtag(any()) } returns null
		coEvery { upcomingMoviesDataSource.insertUpcomingMovies(any(), any()) } returns Unit
		coEvery { upcomingMoviesDataSource.insertUpcomingMoviesPageData(any()) } returns Unit
		coEvery { upcomingMoviesDataSource.getUpcomingMovies(any()) } returns flowOf(null)
		coEvery { nowPlayingMoviesDataSource.getEtag(any()) } returns null
		coEvery { nowPlayingMoviesDataSource.insertNowPlayingMovies(any(), any()) } returns Unit
		coEvery { nowPlayingMoviesDataSource.insertNowPlayingMoviesPageData(any()) } returns Unit
		coEvery { nowPlayingMoviesDataSource.getNowPlayingMovies(any()) } returns flowOf(null)
		coEvery { upcomingMoviesRemoteDataSource.getUpcomingMovies(any(), any()) } returns Ok(
			PagingReply(MovieMother.createUpcomingMovieList(), true, PageData())
		)
		coEvery { popularMoviesRemoteDataSource.getPopularMovies(any(), any()) } returns Ok(
			PagingReply(MovieMother.createPopularMovieList(), true, PageData())
		)
		coEvery { nowPlayingMoviesRemoteDataSource.getNowPlayingMovies(any(), any()) } returns Ok(
			PagingReply(MovieMother.createNowPlayingMovieList(), true, PageData())
		)
		moviesAccessor = MoviesAccessor(
			moviesService,
			popularMoviesDataSource,
			popularMoviesRemoteDataSource,
			nowPlayingMoviesDataSource,
			nowPlayingMoviesRemoteDataSource,
			upcomingMoviesDataSource,
			upcomingMoviesRemoteDataSource,
		)
	}

	@Test
	fun `when getPopularMovies called then returns correct result`() = runTest {
		val popularMoviesFlow = moviesAccessor.getPopularMovies(1)
		val pagingData = MovieMother.createPopularMovieList()
		popularMoviesFlow.first().value.pagingList shouldBeEqual pagingData
	}

	@Test
	fun `when getUpcomingMovies called then returns correct result`() = runTest {
		val upcomingMoviesFlow = moviesAccessor.getUpcomingMovies(1)
		val pagingData = MovieMother.createUpcomingMovieList()
		upcomingMoviesFlow.first().value.pagingList shouldBeEqual pagingData
	}

	@Test
	fun `when getNowPlayingMovies called then returns correct result`() = runTest {
		val nowPlayingMoviesFlow = moviesAccessor.getNowPlayingMovies(1)
		val pagingData = MovieMother.createNowPlayingMovieList()
		nowPlayingMoviesFlow.first().value.pagingList shouldBeEqual pagingData
	}

	@Test
	fun `when getPopularMovies fails but local has data then returns local data`() = runTest {
		coEvery { popularMoviesRemoteDataSource.getPopularMovies(any(), any()) } returns
			Err(Failure.ServerError("Network error"))

		val localPopularMovies = MovieMother.createPopularMovieList()
		val localPagingReply =
			PagingReply(localPopularMovies, true, PageData(page = 1, totalPages = 1, totalResults = 3))
		coEvery { popularMoviesDataSource.getPopularMovies(any()) } returns flowOf(localPagingReply)

		val popularMoviesFlow = moviesAccessor.getPopularMovies(1)

		val result = popularMoviesFlow.first()
		result.value.pagingList shouldBeEqual localPopularMovies
		result.value.isLastPage shouldBeEqual true
		result.value.pageData.page shouldBeEqual 1
	}

	@Test
	fun `when getMovieDetails is called and service is successful, it returns movie details`() = runTest {
		val movieId = 123
		val sessionId = "session-id"
		val ratedDto = RatedDto(8.0f)
		val ratedJsonElement = Json.encodeToJsonElement(RatedDto.serializer(), ratedDto)
		val movieDetailsDto = MovieDtoMother.createMovieDetailsDto(
			id = movieId,
			title = "Test Movie",
			accountStates = AccountStatesDto(
				rated = ratedJsonElement,
				favorite = true,
				watchlist = true
			)
		)
		coEvery {
			moviesService.getMovieDetails(
				movieId,
				ifNoneMatch = null,
				sessionId = sessionId,
				appendToResponse = "account_states"
			)
		} returns movieDetailsDto

		val result = moviesAccessor.getMovieDetails(movieId, sessionId)

		result.isOk shouldBe true
		val movieDetail = (result as Ok<MovieDetail>).value
		movieDetail.id shouldBe movieId
		movieDetail.title shouldBe "Test Movie"
		movieDetail.personalRating shouldBe 8.0f
		movieDetail.favorite shouldBe true
		movieDetail.watchlist shouldBe true
	}

	@Test
	fun `when getMovieDetails is called and service fails, it returns a failure`() = runTest {
		val movieId = 123
		val sessionId = "session-id"
		coEvery {
			moviesService.getMovieDetails(
				movieId,
				ifNoneMatch = null,
				sessionId = sessionId,
				appendToResponse = "account_states"
			)
		} throws RuntimeException("Network error")

		val result = try {
			 moviesAccessor.getMovieDetails(movieId, sessionId)
		} catch (e: Exception) {
			Err<Failure>(Failure.UnknownHostFailure)
		}

		result.isOk shouldBe false
		val error = (result as Err<Failure>).error
		error.shouldBeInstanceOf<Failure.UnknownHostFailure>()
	}

}
