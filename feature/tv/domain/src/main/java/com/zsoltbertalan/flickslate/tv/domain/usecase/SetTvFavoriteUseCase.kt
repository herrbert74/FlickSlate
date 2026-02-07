package com.zsoltbertalan.flickslate.tv.domain.usecase

import com.github.michaelbull.result.coroutines.coroutineBinding
import com.zsoltbertalan.flickslate.account.domain.usecase.GetAccountIdUseCase
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.tv.domain.api.TvFavoritesRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.async

class SetTvFavoriteUseCase @Inject constructor(
	private val tvFavoritesRepository: TvFavoritesRepository,
	private val getAccountIdUseCase: GetAccountIdUseCase,
	private val getSessionIdUseCase: GetSessionIdUseCase,
) {

	suspend fun execute(tvId: Int, favorite: Boolean): Outcome<Unit> {
		return coroutineBinding {
			val accountId = async { getAccountIdUseCase.execute().bind() }
			val sessionId = async { getSessionIdUseCase.execute().bind() }
			tvFavoritesRepository.setTvFavorite(accountId.await(), sessionId.await(), tvId, favorite).bind()
		}
	}

}
