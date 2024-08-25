package com.zsoltbertalan.flickslate.shared.data.getresult

import com.zsoltbertalan.flickslate.shared.testhelper.GenreDtoMother
import com.zsoltbertalan.flickslate.search.data.network.model.GenreReplyDto
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
