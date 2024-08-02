package com.zsoltbertalan.flickslate.data.repository.getresult

import com.zsoltbertalan.flickslate.common.testhelper.GenreDtoMother
import com.zsoltbertalan.flickslate.data.network.dto.GenreReply
import kotlinx.coroutines.delay
import retrofit2.HttpException

fun makeNetworkRequest(): suspend () -> GenreReply = suspend {
	GenreReply(GenreDtoMother.createGenreDtoList())
}

suspend fun makeNetworkRequestDelayed(): suspend () -> GenreReply = suspend {
	delay(1000)
	GenreReply(GenreDtoMother.createGenreDtoList())
}

fun failNetworkRequest(): () -> GenreReply = { throw HttpException(failNetworkRequestResponse()()) }
