package com.zsoltbertalan.flickslate.movies.ui.moviedetails

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetailWithImages
import com.zsoltbertalan.flickslate.movies.domain.usecase.ChangeMovieRatingUseCase
import com.zsoltbertalan.flickslate.movies.domain.usecase.DeleteMovieRatingUseCase
import com.zsoltbertalan.flickslate.movies.domain.usecase.MovieDetailsUseCase
import com.zsoltbertalan.flickslate.movies.domain.usecase.RateMovieUseCase
import com.zsoltbertalan.flickslate.movies.domain.usecase.SetMovieFavoriteUseCase
import com.zsoltbertalan.flickslate.base.kotlin.result.Failure
import com.zsoltbertalan.flickslate.shared.ui.compose.component.rating.RatingToastMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
	private val savedStateHandle: SavedStateHandle,
	private val movieDetailsUseCase: MovieDetailsUseCase,
	private val rateMovieUseCase: RateMovieUseCase,
	private val changeMovieRatingUseCase: ChangeMovieRatingUseCase,
	private val deleteMovieRatingUseCase: DeleteMovieRatingUseCase,
	private val setMovieFavoriteUseCase: SetMovieFavoriteUseCase,
	private val getSessionIdUseCase: GetSessionIdUseCase
) : ViewModel() {

	private val _movieStateData = MutableStateFlow(MovieDetailState())
	internal val movieStateData = _movieStateData.asStateFlow()

	private val movieId: Int
		get() = checkNotNull(savedStateHandle["movieId"])

	fun load(id: Int) {
		if (savedStateHandle.get<Int>("movieId") == id && _movieStateData.value.movieDetail != null) return
		savedStateHandle["movieId"] = id
		getMovieDetail()
		checkLoginStatus()
	}

	private fun checkLoginStatus() {
		viewModelScope.launch {
			val sessionIdResult = getSessionIdUseCase.execute()
			_movieStateData.update { state ->
				val detail = state.movieDetail
				state.copy(
					isLoggedIn = sessionIdResult.isOk,
					isRated = detail?.personalRating?.takeIf { it > -1f } != null,
					lastRatedValue = detail?.personalRating?.takeIf { it > -1f }
				)
			}
		}
	}

	internal fun rateMovie(rating: Float) {
		viewModelScope.launch {
			_movieStateData.update {
				it.copy(
					isRatingInProgress = true,
					failure = null,
					showRatingToast = false,
					lastRatedValue = rating
				)
			}
			val rateMovieResult = rateMovieUseCase.execute(movieId, rating)
			rateMovieResult
				.onSuccess {
					_movieStateData.update {
						it.copy(
							isRatingInProgress = false,
							isRated = true,
							movieDetail = it.movieDetail?.copy(personalRating = rating),
							showRatingToast = true,
							ratingToastMessage = RatingToastMessage.Success
						)
					}
				}
				.onFailure { failure ->
					_movieStateData.update {
						it.copy(
							isRatingInProgress = false,
							failure = failure
						)
					}
				}
		}
	}

	internal fun changeRating(rating: Float) {
		viewModelScope.launch {
			_movieStateData.update {
				it.copy(
					isRatingInProgress = true,
					failure = null,
					showRatingToast = false,
					lastRatedValue = rating
				)
			}
			val changeResult = changeMovieRatingUseCase.execute(movieId, rating)
			changeResult
				.onSuccess {
					_movieStateData.update {
						it.copy(
							isRatingInProgress = false,
							isRated = true,
							movieDetail = it.movieDetail?.copy(personalRating = rating),
							showRatingToast = true,
							ratingToastMessage = RatingToastMessage.Updated
						)
					}
				}
				.onFailure { failure ->
					_movieStateData.update { it.copy(isRatingInProgress = false, failure = failure) }
				}
		}
	}

	internal fun deleteRating() {
		viewModelScope.launch {
			_movieStateData.update {
				it.copy(
					isRatingInProgress = true,
					failure = null,
					showRatingToast = false
				)
			}
			val deleteResult = deleteMovieRatingUseCase.execute(movieId)
			deleteResult
				.onSuccess {
					_movieStateData.update {
						it.copy(
							isRatingInProgress = false,
							isRated = false,
							movieDetail = it.movieDetail?.copy(personalRating = -1f),
							lastRatedValue = null,
							showRatingToast = true,
							ratingToastMessage = RatingToastMessage.Deleted
						)
					}
				}
				.onFailure { failure ->
					_movieStateData.update { it.copy(isRatingInProgress = false, failure = failure) }
				}
		}
	}

	internal fun toggleFavorite() {
		val isFavorite = _movieStateData.value.isFavorite
		val newFavoriteValue = !isFavorite
		viewModelScope.launch {
			_movieStateData.update {
				it.copy(
					isFavoriteInProgress = true,
					failure = null
				)
			}
			val result = setMovieFavoriteUseCase.execute(movieId, newFavoriteValue)
			result
				.onSuccess {
					_movieStateData.update {
						it.copy(
							isFavoriteInProgress = false,
							isFavorite = newFavoriteValue,
							movieDetail = it.movieDetail?.copy(favorite = newFavoriteValue),
							showFavoriteToast = true
						)
					}
				}
				.onFailure { failure ->
					_movieStateData.update { it.copy(isFavoriteInProgress = false, failure = failure) }
				}
		}
	}

	private fun getMovieDetail() {
		viewModelScope.launch {
			val movieDetailsResult = movieDetailsUseCase.getMovieDetails(movieId)
			movieDetailsResult
				.onSuccess { movieDetail ->
					_movieStateData.update { movieDetailState ->
						movieDetailState.copy(
							movieDetail = movieDetail,
							isRated = movieDetail.personalRating > -1f,
							lastRatedValue = movieDetail.personalRating.takeIf { it > -1f },
							isFavorite = movieDetail.favorite,
							isWatchlist = movieDetail.watchlist,
							failure = null
						)
					}
				}
				.onFailure { failure ->
					_movieStateData.update {
						it.copy(
							movieDetail = null,
							failure = failure
						)
					}
				}
		}
	}

	internal fun toastShown() {
		_movieStateData.update { it.copy(showRatingToast = false, ratingToastMessage = null, showFavoriteToast = false) }
	}
}

@Immutable
internal data class MovieDetailState(
	val movieDetail: MovieDetailWithImages? = null,
	val isRatingInProgress: Boolean = false,
	val isRated: Boolean = false,
	val isLoggedIn: Boolean = false,
	val failure: Failure? = null,
	val isFavorite: Boolean = false,
	val isFavoriteInProgress: Boolean = false,
	val isWatchlist: Boolean = false,
	val showRatingToast: Boolean = false,
	val ratingToastMessage: RatingToastMessage? = null,
	val showFavoriteToast: Boolean = false,
	val lastRatedValue: Float? = null,
)
