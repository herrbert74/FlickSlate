package com.zsoltbertalan.flickslate.movies.domain.usecase

import com.github.michaelbull.result.coroutines.coroutineBinding
import com.zsoltbertalan.flickslate.account.domain.usecase.GetAccountIdUseCase
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.movies.domain.api.MovieFavoritesRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.async

@Inject
class SetMovieFavoriteUseCase internal constructor(
	private val movieFavoritesRepository: MovieFavoritesRepository,
	private val getAccountIdUseCase: GetAccountIdUseCase,
	private val getSessionIdUseCase: GetSessionIdUseCase,
) {

	suspend fun execute(movieId: Int, favorite: Boolean): Outcome<Unit> {
		return coroutineBinding {
			val accountId = async { getAccountIdUseCase.execute().bind() }
			val sessionId = async { getSessionIdUseCase.execute().bind() }
			movieFavoritesRepository.setMovieFavorite(accountId.await(), sessionId.await(), movieId, favorite).bind()
		}
	}
}
