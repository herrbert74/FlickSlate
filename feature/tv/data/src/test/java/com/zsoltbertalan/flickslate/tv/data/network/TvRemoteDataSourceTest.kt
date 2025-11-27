package com.zsoltbertalan.flickslate.tv.data.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.shared.data.network.model.AccountStatesDto
import com.zsoltbertalan.flickslate.shared.data.network.model.ErrorBody
import com.zsoltbertalan.flickslate.shared.data.network.model.RatedDto
import com.zsoltbertalan.flickslate.shared.data.network.model.TvEpisodeDetailsDto
import com.zsoltbertalan.flickslate.shared.data.network.model.images.toImagesReply
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import com.zsoltbertalan.flickslate.tv.data.network.model.TOTAL_PAGES
import com.zsoltbertalan.flickslate.tv.data.network.model.TOTAL_RESULTS
import com.zsoltbertalan.flickslate.tv.data.network.model.TopRatedTvReplyDto
import com.zsoltbertalan.flickslate.tv.data.network.model.TvDetailsDtoMother
import com.zsoltbertalan.flickslate.tv.data.network.model.TvDtoMother
import com.zsoltbertalan.flickslate.tv.data.network.model.TvSeasonDetailsDtoMother
import com.zsoltbertalan.flickslate.tv.data.network.model.images.ImagesReplyDtoMother
import com.zsoltbertalan.flickslate.tv.data.network.model.toTvDetail
import com.zsoltbertalan.flickslate.tv.data.network.model.toTvSeasonDetails
import com.zsoltbertalan.flickslate.shared.data.network.model.toTvEpisodeDetail
import com.zsoltbertalan.flickslate.tv.domain.model.TvMother
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class TvRemoteDataSourceTest {

	private val tvService: TvService = mockk()
	private lateinit var tvDataSource: TvRemoteDataSource

	@Before
	fun setup() {
		coEvery { tvService.getTopRatedTv(any(), any(), any()) } returns Response.success(
			TvDtoMother.createTopRatedTvReplyDto()
		)
		tvDataSource = TvRemoteDataSource(tvService)
	}

	@Test
	fun `when getTopRatedTv called and service returns result then returns correct result`() = runTest {
		val result = tvDataSource.getTopRatedTv("", 1)
		result.value.pagingList shouldBeEqual TvMother.createTvList()
		result.value.isLastPage shouldBe false
		result.value.pageData shouldBe PageData(totalPages = TOTAL_PAGES, totalResults = TOTAL_RESULTS)
	}

	@Test
	fun `when getTopRatedTv called and service returns failure then returns correct result`() = runTest {
		coEvery { tvService.getTopRatedTv(any(), any(), any()) } returns failNetworkRequestResponse()()
		tvDataSource.getTopRatedTv("", 1) shouldBeEqual
			Err(
				Failure.ServerError(
					"""
						{"success":false,
						"status_code":6,"status_message":"Invalid id: The pre-requisite id is invalid
						 or not found."}
					"""
						.trimIndent()
						.filterNot { it == '\n' }
				)
			)
	}

	@Test
	fun `getTvDetails returns mapped detail with rating`() = runTest {
		val dto = TvDetailsDtoMother.createTvDetailsDto(accountStatesRating = 8.0f)
		coEvery { tvService.getTvDetails(any(), any(), any(), any(), any()) } returns dto

		tvDataSource.getTvDetails(seriesId = 1, sessionId = "session") shouldBeEqual Ok(dto.toTvDetail())
	}

	@Test
	fun `getTvImages returns mapped images`() = runTest {
		val imagesDto = ImagesReplyDtoMother.createImagesReplyDto()
		coEvery { tvService.getTvImages(any(), any()) } returns imagesDto

		tvDataSource.getTvImages(seriesId = 1) shouldBeEqual Ok(imagesDto.toImagesReply())
	}

	@Test
	fun `getTvSeasonDetails returns mapped season`() = runTest {
		val seasonDto = TvSeasonDetailsDtoMother.createSeasonDetailsDto()
		coEvery { tvService.getTvSeasonDetails(any(), any(), any(), any()) } returns Response.success(seasonDto)

		tvDataSource.getTvSeasonDetails(tvId = 1, tvSeasonNumber = 1) shouldBeEqual Ok(seasonDto.toTvSeasonDetails())
	}

	@Test
	fun `getTvEpisodeDetail maps account state rating`() = runTest {
		val episodeDto = TvEpisodeDetailsDto(
			id = 10,
			show_id = 20,
			season_number = 2,
			episode_number = 3,
			account_states = AccountStatesDto(
				rated = Json.encodeToJsonElement(RatedDto.serializer(), RatedDto(value = 7.0f))
			)
		)
		coEvery { tvService.getTvEpisodeDetails(any(), any(), any(), any(), any(), any(), any()) } returns episodeDto

		tvDataSource.getTvEpisodeDetail(1, 2, 3, "session") shouldBeEqual Ok(episodeDto.toTvEpisodeDetail())
	}

	@Suppress("unused")
	private fun failNetworkRequestResponse(): () -> Response<TopRatedTvReplyDto> = {
		val errorBody = ErrorBody(
			success = false,
			status_code = 6,
			status_message = "Invalid id: The pre-requisite id is invalid or not found."
		)
		Response.error(404, Json.encodeToString(errorBody).toResponseBody())
	}
}
