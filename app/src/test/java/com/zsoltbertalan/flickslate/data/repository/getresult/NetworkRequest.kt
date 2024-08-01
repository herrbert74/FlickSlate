package com.zsoltbertalan.flickslate.data.repository.getresult

import com.zsoltbertalan.flickslate.common.testhelper.GenreDtoMother
import com.zsoltbertalan.flickslate.data.network.dto.GenreResponse
import kotlinx.coroutines.delay
import retrofit2.HttpException

fun makeNetworkRequest(): suspend () -> GenreResponse = suspend {
	GenreResponse(GenreDtoMother.createGenreDtoList())
}

suspend fun makeNetworkRequestDelayed(): suspend () -> GenreResponse = suspend {
	delay(1000)
	GenreResponse(GenreDtoMother.createGenreDtoList())
}

fun failNetworkRequest(): () -> GenreResponse = { throw HttpException(failNetworkRequestResponse()()) }
