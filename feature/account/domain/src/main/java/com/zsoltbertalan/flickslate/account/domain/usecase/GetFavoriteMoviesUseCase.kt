package com.zsoltbertalan.flickslate.account.domain.usecase

import com.github.michaelbull.result.coroutines.coroutineBinding
import com.zsoltbertalan.flickslate.account.domain.api.FavoritesRepository
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteMovie
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.kotlin.result.Outcome
import kotlinx.coroutines.async
import javax.inject.Inject

class GetFavoriteMoviesUseCase @Inject constructor(
	private val favoritesRepository: FavoritesRepository,
	private val getAccountIdUseCase: GetAccountIdUseCase,
	private val getSessionIdUseCase: GetSessionIdUseCase,
) {

	suspend fun execute(page: Int): Outcome<PagingReply<FavoriteMovie>> {
		return coroutineBinding {
			val accountId = async { getAccountIdUseCase.execute().bind() }
			val sessionId = async { getSessionIdUseCase.execute().bind() }
			favoritesRepository.getFavoriteMovies(accountId.await(), sessionId.await(), page).bind()
		}
	}
}
