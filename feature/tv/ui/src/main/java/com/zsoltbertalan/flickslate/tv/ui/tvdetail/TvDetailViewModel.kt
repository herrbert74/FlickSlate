package com.zsoltbertalan.flickslate.tv.ui.tvdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetailWithImages
import com.zsoltbertalan.flickslate.tv.domain.usecase.ChangeTvRatingUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.DeleteTvRatingUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.RateTvShowUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.SetTvFavoriteUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.TvDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val SERIES_ID_ARG = "seriesId"
const val SEASON_NUMBER_ARG = "seasonNumber"
const val EPISODE_NUMBER_ARG = "episodeNumber"

@HiltViewModel
class TvDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val tvDetailsUseCase: TvDetailsUseCase,
	private val rateTvShowUseCase: RateTvShowUseCase,
	private val changeTvRatingUseCase: ChangeTvRatingUseCase,
	private val deleteTvRatingUseCase: DeleteTvRatingUseCase,
	private val setTvFavoriteUseCase: SetTvFavoriteUseCase,
	private val getSessionIdUseCase: GetSessionIdUseCase,
) : ViewModel() {

	private val seriesId: Int = checkNotNull(savedStateHandle[SERIES_ID_ARG])
	private val seasonNumber: Int? = savedStateHandle[SEASON_NUMBER_ARG]
	private val episodeNumber: Int? = savedStateHandle[EPISODE_NUMBER_ARG]

	private val _tvStateData = MutableStateFlow(
		TvDetailState(seasonNumber = seasonNumber, episodeNumber = episodeNumber)
	)
	val tvStateData = _tvStateData.asStateFlow()

	init {
		getTvDetail()
		checkLoginStatus()
	}

	private fun checkLoginStatus() {
		viewModelScope.launch {
			val sessionIdResult = getSessionIdUseCase.execute()
			_tvStateData.update { it.copy(isLoggedIn = sessionIdResult.isOk) }
		}
	}

	internal fun rateTvShow(rating: Float) {
		viewModelScope.launch {
			_tvStateData.update { it.copy(isRatingInProgress = true, isRated = false, failure = null) }
			val rateResult = rateTvShowUseCase.execute(seriesId, rating)
			when {
				rateResult.isOk -> _tvStateData.update {
					it.copy(
						isRatingInProgress = false,
						isRated = true,
						showRatingToast = true,
						lastRatedValue = rating,
						tvDetail = it.tvDetail?.copy(personalRating = rating),
					)
				}

				else -> _tvStateData.update { it.copy(isRatingInProgress = false, failure = rateResult.error) }
			}
		}
	}

	private fun getTvDetail() {
		viewModelScope.launch {
			val tvDetailsResult = tvDetailsUseCase.getTvDetails(seriesId)
			when {
				tvDetailsResult.isOk -> _tvStateData.update {
					val detail = tvDetailsResult.value
					it.copy(
						tvDetail = detail,
						isRated = detail.personalRating > -1f,
						isFavorite = detail.favorite,
						lastRatedValue = null,
						failure = null
					)
				}

				else -> _tvStateData.update {
					it.copy(
						tvDetail = null,
						failure = tvDetailsResult.error
					)
				}
			}

		}
	}

	internal fun toastShown() {
		_tvStateData.update { it.copy(showRatingToast = false) }
	}

	internal fun changeTvRating(rating: Float) {
		viewModelScope.launch {
			_tvStateData.update { it.copy(isRatingInProgress = true, failure = null) }
			val result = changeTvRatingUseCase.execute(seriesId, rating)
			when {
				result.isOk -> _tvStateData.update {
					it.copy(
						isRatingInProgress = false,
						isRated = true,
						showRatingToast = true,
						lastRatedValue = rating,
						tvDetail = it.tvDetail?.copy(personalRating = rating),
					)
				}
				else -> _tvStateData.update { it.copy(isRatingInProgress = false, failure = result.error) }
			}
		}
	}

	internal fun deleteTvRating() {
		viewModelScope.launch {
			_tvStateData.update { it.copy(isRatingInProgress = true, failure = null) }
			val result = deleteTvRatingUseCase.execute(seriesId)
			when {
				result.isOk -> _tvStateData.update {
					it.copy(
						isRatingInProgress = false,
						isRated = false,
						showRatingToast = true,
						lastRatedValue = null,
						tvDetail = it.tvDetail?.copy(personalRating = -1f),
					)
				}
				else -> _tvStateData.update { it.copy(isRatingInProgress = false, failure = result.error) }
			}
		}
	}

	internal fun toggleFavorite() {
		val isFavorite = _tvStateData.value.isFavorite
		val newFavoriteValue = !isFavorite
		viewModelScope.launch {
			_tvStateData.update {
				it.copy(
					isFavoriteInProgress = true,
					failure = null
				)
			}
			val result = setTvFavoriteUseCase.execute(seriesId, newFavoriteValue)
			when {
				result.isOk -> _tvStateData.update {
					it.copy(
						isFavoriteInProgress = false,
						isFavorite = newFavoriteValue,
						tvDetail = it.tvDetail?.copy(favorite = newFavoriteValue)
					)
				}

				else -> _tvStateData.update {
					it.copy(
						isFavoriteInProgress = false,
						failure = result.error
					)
				}
			}
		}
	}
}

data class TvDetailState(
	val tvDetail: TvDetailWithImages? = null,
	val failure: Failure? = null,
	val seasonNumber: Int? = null,
	val episodeNumber: Int? = null,
	// rating + login state
	val isRatingInProgress: Boolean = false,
	val isRated: Boolean = false,
	val isLoggedIn: Boolean = false,
	val isFavorite: Boolean = false,
	val isFavoriteInProgress: Boolean = false,
	val showRatingToast: Boolean = false,
	val lastRatedValue: Float? = null,
)
