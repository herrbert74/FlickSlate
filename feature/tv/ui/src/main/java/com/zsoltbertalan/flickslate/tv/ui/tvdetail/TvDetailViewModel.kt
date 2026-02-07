package com.zsoltbertalan.flickslate.tv.ui.tvdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.CreationExtras.Key
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.base.kotlin.result.Failure
import com.zsoltbertalan.flickslate.shared.domain.di.AppScope
import com.zsoltbertalan.flickslate.shared.ui.compose.component.rating.RatingToastMessage
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetailWithImages
import com.zsoltbertalan.flickslate.tv.domain.usecase.ChangeTvRatingUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.DeleteTvRatingUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.RateTvShowUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.SetTvFavoriteUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.TvDetailsUseCase
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val SERIES_ID_ARG = "seriesId"
const val SEASON_NUMBER_ARG = "seasonNumber"
const val EPISODE_NUMBER_ARG = "episodeNumber"

@AssistedInject
class TvDetailViewModel(
	@Assisted private val savedStateHandle: SavedStateHandle,
	private val tvDetailsUseCase: TvDetailsUseCase,
	private val rateTvShowUseCase: RateTvShowUseCase,
	private val changeTvRatingUseCase: ChangeTvRatingUseCase,
	private val deleteTvRatingUseCase: DeleteTvRatingUseCase,
	private val setTvFavoriteUseCase: SetTvFavoriteUseCase,
	private val getSessionIdUseCase: GetSessionIdUseCase,
) : ViewModel() {

	@AssistedFactory
	@ViewModelAssistedFactoryKey(TvDetailViewModel::class)
	@ContributesIntoMap(AppScope::class)
	fun interface Factory : ViewModelAssistedFactory {
		override fun create(extras: CreationExtras): TvDetailViewModel {
			return create(extras.createSavedStateHandle())
		}
		fun create(@Assisted savedStateHandle: SavedStateHandle): TvDetailViewModel
	}

	private val seriesId: Int
		get() = checkNotNull(savedStateHandle[SERIES_ID_ARG])
	private val seasonNumber: Int?
		get() = savedStateHandle[SEASON_NUMBER_ARG]
	private val episodeNumber: Int?
		get() = savedStateHandle[EPISODE_NUMBER_ARG]

	private val _tvStateData = MutableStateFlow(
		TvDetailState(seasonNumber = seasonNumber, episodeNumber = episodeNumber)
	)
	val tvStateData = _tvStateData.asStateFlow()

	fun load(id: Int, season: Int? = null, episode: Int? = null) {
		val isSameArgs = savedStateHandle.get<Int>(SERIES_ID_ARG) == id && seasonNumber == season && episodeNumber == episode
		if (isSameArgs && _tvStateData.value.tvDetail != null) return
		savedStateHandle[SERIES_ID_ARG] = id
		if (season != null) savedStateHandle[SEASON_NUMBER_ARG] = season
		if (episode != null) savedStateHandle[EPISODE_NUMBER_ARG] = episode
		_tvStateData.update { it.copy(seasonNumber = season, episodeNumber = episode) }
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
			rateResult
				.onSuccess {
					_tvStateData.update {
						it.copy(
							isRatingInProgress = false,
							isRated = true,
							showRatingToast = true,
							ratingToastMessage = RatingToastMessage.Success,
							lastRatedValue = rating,
							tvDetail = it.tvDetail?.copy(personalRating = rating),
						)
					}
				}
				.onFailure { failure ->
					_tvStateData.update { it.copy(isRatingInProgress = false, failure = failure) }
				}
		}
	}

	private fun getTvDetail() {
		viewModelScope.launch {
			val tvDetailsResult = tvDetailsUseCase.getTvDetails(seriesId)
			tvDetailsResult
				.onSuccess { detail ->
					_tvStateData.update {
						it.copy(
							tvDetail = detail,
							isRated = detail.personalRating > -1f,
							isFavorite = detail.favorite,
							lastRatedValue = null,
							failure = null
						)
					}
				}
				.onFailure { failure ->
					_tvStateData.update {
						it.copy(
							tvDetail = null,
							failure = failure
						)
					}
				}

		}
	}

	internal fun toastShown() {
		_tvStateData.update { it.copy(showRatingToast = false, ratingToastMessage = null) }
	}

	internal fun changeTvRating(rating: Float) {
		viewModelScope.launch {
			_tvStateData.update { it.copy(isRatingInProgress = true, failure = null) }
			val result = changeTvRatingUseCase.execute(seriesId, rating)
			result
				.onSuccess {
					_tvStateData.update {
						it.copy(
							isRatingInProgress = false,
							isRated = true,
							showRatingToast = true,
							ratingToastMessage = RatingToastMessage.Updated,
							lastRatedValue = rating,
							tvDetail = it.tvDetail?.copy(personalRating = rating),
						)
					}
				}
				.onFailure { failure ->
					_tvStateData.update { it.copy(isRatingInProgress = false, failure = failure) }
				}
		}
	}

	internal fun deleteTvRating() {
		viewModelScope.launch {
			_tvStateData.update { it.copy(isRatingInProgress = true, failure = null) }
			val result = deleteTvRatingUseCase.execute(seriesId)
			result
				.onSuccess {
					_tvStateData.update {
						it.copy(
							isRatingInProgress = false,
							isRated = false,
							showRatingToast = true,
							ratingToastMessage = RatingToastMessage.Deleted,
							lastRatedValue = null,
							tvDetail = it.tvDetail?.copy(personalRating = -1f),
						)
					}
				}
				.onFailure { failure ->
					_tvStateData.update { it.copy(isRatingInProgress = false, failure = failure) }
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
			result
				.onSuccess {
					_tvStateData.update {
						it.copy(
							isFavoriteInProgress = false,
							isFavorite = newFavoriteValue,
							tvDetail = it.tvDetail?.copy(favorite = newFavoriteValue),
							showFavoriteToast = true
						)
					}
				}
				.onFailure { failure ->
					_tvStateData.update { it.copy(isFavoriteInProgress = false, failure = failure) }
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
	val ratingToastMessage: RatingToastMessage? = null,
	val showFavoriteToast: Boolean = false,
	val lastRatedValue: Float? = null,
)
