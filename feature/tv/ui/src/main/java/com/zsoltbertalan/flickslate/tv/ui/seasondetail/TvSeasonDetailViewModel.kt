package com.zsoltbertalan.flickslate.tv.ui.seasondetail

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import com.zsoltbertalan.flickslate.tv.domain.model.SeasonDetail
import com.zsoltbertalan.flickslate.tv.domain.usecase.GetSeasonDetailUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.RateTvShowEpisodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val SERIES_ID_ARG = "seriesId"
const val SEASON_NUMBER_ARG = "seasonNumber"
const val SEASON_TITLE_ARG = "seasonTitle"
const val BG_COLOR_ARG = "bgColor"
const val BG_COLOR_DIM_ARG = "bgColorDim"

@HiltViewModel
class TvSeasonDetailViewModel @Inject constructor(
	private val getSeasonDetailUseCase: GetSeasonDetailUseCase,
	private val rateEpisodeUseCase: RateTvShowEpisodeUseCase,
	private val getSessionIdUseCase: GetSessionIdUseCase,
	savedStateHandle: SavedStateHandle,
) : ViewModel() {

	private val seriesId: Int = checkNotNull(savedStateHandle[SERIES_ID_ARG])
	private val seasonNumber: Int = checkNotNull(savedStateHandle[SEASON_NUMBER_ARG])
	private val bgColor: Int = checkNotNull(savedStateHandle[BG_COLOR_ARG])
	private val bgColorDim: Int = checkNotNull(savedStateHandle[BG_COLOR_DIM_ARG])
	private val seasonTitle: String? = savedStateHandle[SEASON_TITLE_ARG]

	private val _uiState = MutableStateFlow(TvSeasonDetailUiState(title = seasonTitle ?: "Season Details"))
	val uiState: StateFlow<TvSeasonDetailUiState> = _uiState.asStateFlow()

	init {
		fetchSeasonDetails()
		checkLoginStatus()
	}

	private fun checkLoginStatus() {
		viewModelScope.launch {
			val sessionIdResult = getSessionIdUseCase.execute()
			_uiState.update { it.copy(isLoggedIn = sessionIdResult.isOk) }
		}
	}

	fun fetchSeasonDetails() {
		_uiState.update { it.copy(isLoading = true, failure = null, bgColor = bgColor, bgColorDim = bgColorDim) }
		viewModelScope.launch {
			val result = getSeasonDetailUseCase.execute(seriesId, seasonNumber)
			when {
				result.isOk -> _uiState.update {
					it.copy(
						isLoading = false,
						seasonDetail = result.value,
						failure = null,
					)
				}

				else -> _uiState.update {
					it.copy(
						isLoading = false,
						failure = result.error
					)
				}
			}
		}
	}

	fun toggleEpisodeExpanded(episodeId: Int) {
		_uiState.update { currentState ->
			val newExpandedId = if (currentState.expandedEpisodeId == episodeId) {
				null // Collapse if already expanded
			} else {
				episodeId // Expand new one
			}
			currentState.copy(expandedEpisodeId = newExpandedId)
		}
	}

	fun rateEpisode(episodeNumber: Int, rating: Float) {
		viewModelScope.launch {
			_uiState.update { it.copy(isRatingInProgress = true, isRated = false, failure = null) }
			val rateResult = rateEpisodeUseCase.execute(seriesId, seasonNumber, episodeNumber, rating)
			when {
				rateResult.isOk -> _uiState.update {
					it.copy(
						isRatingInProgress = false,
						isRated = true,
						showRatingToast = true
					)
				}
				else -> _uiState.update { it.copy(isRatingInProgress = false, failure = rateResult.error) }
			}
		}
	}

	fun toastShown() {
		_uiState.update { it.copy(showRatingToast = false) }
	}
}

@Immutable
data class TvSeasonDetailUiState(
	val isLoading: Boolean = true,
	val title: String? = null,
	val seasonDetail: SeasonDetail? = null,
	val bgColor: Int = 0,
	val bgColorDim: Int = 0,
	val failure: Failure? = null,
	val expandedEpisodeId: Int? = null,
	// rating + login state
	val isRatingInProgress: Boolean = false,
	val isRated: Boolean = false,
	val isLoggedIn: Boolean = false,
	val showRatingToast: Boolean = false,
)
