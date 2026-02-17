package com.zsoltbertalan.flickslate.account.domain.usecase

import com.github.michaelbull.result.coroutines.coroutineBinding
import com.zsoltbertalan.flickslate.account.domain.api.FavoritesRepository
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteTvShow
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.async

@Inject
class GetFavoriteTvShowsUseCase internal constructor(
	private val favoritesRepository: FavoritesRepository,
	private val getAccountIdUseCase: GetAccountIdUseCase,
	private val getSessionIdUseCase: GetSessionIdUseCase,
) {

	suspend fun execute(page: Int): Outcome<PagingReply<FavoriteTvShow>> {
		return coroutineBinding {
			val accountId = async { getAccountIdUseCase.execute().bind() }
			val sessionId = async { getSessionIdUseCase.execute().bind() }
			favoritesRepository.getFavoriteTvShows(accountId.await(), sessionId.await(), page).bind()
		}
	}
}
