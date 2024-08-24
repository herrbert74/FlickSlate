package com.zsoltbertalan.flickslate.data.repository.getresult

import com.zsoltbertalan.flickslate.common.testhelper.GenreDtoMother
import com.zsoltbertalan.flickslate.data.network.dto.GenreReplyDto
import kotlinx.coroutines.delay
import retrofit2.HttpException

fun makeNetworkRequest(): suspend () -> GenreReplyDto = suspend {
	GenreReplyDto(GenreDtoMother.createGenreDtoList())
}

suspend fun makeNetworkRequestDelayed(): suspend () -> GenreReplyDto = suspend {
	delay(1000)
	GenreReplyDto(GenreDtoMother.createGenreDtoList())
}

fun failNetworkRequest(): () -> GenreReplyDto = { throw HttpException(failNetworkRequestResponse()()) }
