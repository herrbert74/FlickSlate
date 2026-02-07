package com.zsoltbertalan.flickslate.tv.ui.seasondetail

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.base.kotlin.result.Failure
import com.zsoltbertalan.flickslate.shared.domain.di.AppScope
import com.zsoltbertalan.flickslate.shared.ui.compose.component.rating.RatingToastMessage
import com.zsoltbertalan.flickslate.tv.domain.model.SeasonDetail
import com.zsoltbertalan.flickslate.tv.domain.usecase.ChangeTvShowEpisodeRatingUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.DeleteTvShowEpisodeRatingUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.GetEpisodeDetailUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.GetSeasonDetailUseCase
import com.zsoltbertalan.flickslate.tv.domain.usecase.RateTvShowEpisodeUseCase
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val SERIES_ID_ARG = "seriesId"
const val SEASON_NUMBER_ARG = "seasonNumber"
const val SEASON_TITLE_ARG = "seasonTitle"
const val BG_COLOR_ARG = "bgColor"
const val BG_COLOR_DIM_ARG = "bgColorDim"

@AssistedInject
class TvSeasonDetailViewModel(
	private val getSeasonDetailUseCase: GetSeasonDetailUseCase,
	private val rateEpisodeUseCase: RateTvShowEpisodeUseCase,
	private val changeEpisodeRatingUseCase: ChangeTvShowEpisodeRatingUseCase,
	private val deleteEpisodeRatingUseCase: DeleteTvShowEpisodeRatingUseCase,
	private val getSessionIdUseCase: GetSessionIdUseCase,
	private val getEpisodeDetailUseCase: GetEpisodeDetailUseCase,
	@Assisted private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

	@AssistedFactory
	@ViewModelAssistedFactoryKey(TvSeasonDetailViewModel::class)
	@ContributesIntoMap(AppScope::class)
	fun interface Factory : ViewModelAssistedFactory {
		override fun create(extras: CreationExtras): TvSeasonDetailViewModel {
			return create(extras.createSavedStateHandle())
		}
		fun create(@Assisted savedStateHandle: SavedStateHandle): TvSeasonDetailViewModel
	}

	private val seriesId: Int
		get() = checkNotNull(savedStateHandle[SERIES_ID_ARG])
	private val seasonNumber: Int
		get() = checkNotNull(savedStateHandle[SEASON_NUMBER_ARG])
	private val bgColor: Int
		get() = savedStateHandle[BG_COLOR_ARG] ?: 0
	private val bgColorDim: Int
		get() = savedStateHandle[BG_COLOR_DIM_ARG] ?: 0
	private val seasonTitle: String?
		get() = savedStateHandle[SEASON_TITLE_ARG]

	private val _uiState = MutableStateFlow(TvSeasonDetailUiState(title = seasonTitle ?: "Season Details"))
	val uiState: StateFlow<TvSeasonDetailUiState> = _uiState.asStateFlow()

	fun load(id: Int, season: Int, color: Int, colorDim: Int) {
		val isSameArgs =
			savedStateHandle.get<Int>(SERIES_ID_ARG) == id && savedStateHandle.get<Int>(SEASON_NUMBER_ARG) == season
		if (isSameArgs && _uiState.value.seasonDetail != null) return
		savedStateHandle[SERIES_ID_ARG] = id
		savedStateHandle[SEASON_NUMBER_ARG] = season
		savedStateHandle[BG_COLOR_ARG] = color
		savedStateHandle[BG_COLOR_DIM_ARG] = colorDim
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
			result
				.onSuccess { seasonDetail ->
					_uiState.update {
						it.copy(
							isLoading = false,
							seasonDetail = seasonDetail,
							failure = null,
						)
					}
				}
				.onFailure { failure ->
					_uiState.update {
						it.copy(
							isLoading = false,
							failure = failure
						)
					}
				}
		}
	}

	fun toggleEpisodeExpanded(episodeId: Int) {
		_uiState.update { currentState ->
			val newExpandedId = if (currentState.expandedEpisodeId == episodeId) null else episodeId
			currentState.copy(expandedEpisodeId = newExpandedId)
		}
		// If expanded, fetch account_states for this episode and merge personal rating
		val episode = _uiState.value.seasonDetail?.episodes?.firstOrNull { it.id == episodeId }
		if (episode != null && _uiState.value.expandedEpisodeId == episodeId) {
			viewModelScope.launch {
				val result = getEpisodeDetailUseCase.execute(seriesId, seasonNumber, episode.episodeNumber)
				result.onSuccess { updated ->
					_uiState.update { state ->
						val updatedEpisodes = state.seasonDetail?.episodes?.map {
							if (it.id == episodeId) it.copy(personalRating = updated.personalRating) else it
						}
						state.copy(
							seasonDetail = state.seasonDetail?.copy(
								episodes = updatedEpisodes ?: state.seasonDetail.episodes
							)
						)
					}
				}
			}
		}
	}

	fun rateEpisode(episodeNumber: Int, rating: Float) {
		viewModelScope.launch {
			_uiState.update { it.copy(isRatingInProgress = true, isRated = false, failure = null) }
			val rateResult = rateEpisodeUseCase.execute(seriesId, seasonNumber, episodeNumber, rating)
			rateResult
				.onSuccess {
					_uiState.update {
						it.copy(
							isRatingInProgress = false,
							isRated = true,
							showRatingToast = true,
							ratingToastMessage = RatingToastMessage.Success,
						)
					}
				}
				.onFailure { failure ->
					_uiState.update { it.copy(isRatingInProgress = false, failure = failure) }
				}
		}
	}

	fun changeEpisodeRating(episodeNumber: Int, rating: Float) {
		viewModelScope.launch {
			_uiState.update { it.copy(isRatingInProgress = true, failure = null) }
			val rateResult = changeEpisodeRatingUseCase.execute(seriesId, seasonNumber, episodeNumber, rating)
			rateResult
				.onSuccess {
					_uiState.update {
						it.copy(
							isRatingInProgress = false,
							showRatingToast = true,
							ratingToastMessage = RatingToastMessage.Updated,
						)
					}
				}
				.onFailure { failure ->
					_uiState.update { it.copy(isRatingInProgress = false, failure = failure) }
				}
		}
	}

	fun deleteEpisodeRating(episodeNumber: Int) {
		viewModelScope.launch {
			_uiState.update { it.copy(isRatingInProgress = true, failure = null) }
			val rateResult = deleteEpisodeRatingUseCase.execute(seriesId, seasonNumber, episodeNumber)
			rateResult
				.onSuccess {
					_uiState.update {
						it.copy(
							isRatingInProgress = false,
							showRatingToast = true,
							ratingToastMessage = RatingToastMessage.Deleted,
						)
					}
				}
				.onFailure { failure ->
					_uiState.update { it.copy(isRatingInProgress = false, failure = failure) }
				}
		}
	}

	fun toastShown() {
		_uiState.update { it.copy(showRatingToast = false, ratingToastMessage = null) }
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
	val ratingToastMessage: RatingToastMessage? = null,
)
