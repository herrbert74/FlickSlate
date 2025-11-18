package com.zsoltbertalan.flickslate.movies.ui.moviedetails

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsoltbertalan.flickslate.account.domain.usecase.GetSessionIdUseCase
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetailWithImages
import com.zsoltbertalan.flickslate.movies.domain.usecase.MovieDetailsUseCase
import com.zsoltbertalan.flickslate.movies.domain.usecase.RateMovieUseCase
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val movieDetailsUseCase: MovieDetailsUseCase,
	private val rateMovieUseCase: RateMovieUseCase,
	private val getSessionIdUseCase: GetSessionIdUseCase
) : ViewModel() {

	private val _movieStateData = MutableStateFlow(MovieDetailState())
	internal val movieStateData = _movieStateData.asStateFlow()

	private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

	init {
		getMovieDetail()
		checkLoginStatus()
	}

	private fun checkLoginStatus() {
		viewModelScope.launch {
			val sessionIdResult = getSessionIdUseCase.execute()
			_movieStateData.update { it.copy(isLoggedIn = sessionIdResult.isOk) }
		}
	}

	internal fun rateMovie(rating: Float) {
		viewModelScope.launch {
			_movieStateData.update { it.copy(isRatingInProgress = true, isRated = false, failure = null) }
			val rateMovieResult = rateMovieUseCase.execute(movieId, rating)
			when {
				rateMovieResult.isOk ->
					_movieStateData.update {
						it.copy(
							isRatingInProgress = false,
							isRated = true,
							movieDetail = it.movieDetail?.copy(personalRating = rating),
							showRatingToast = true
						)
					}

				else ->
					_movieStateData.update { it.copy(isRatingInProgress = false, failure = rateMovieResult.error) }
			}
		}
	}

	private fun getMovieDetail() {
		viewModelScope.launch {
			val movieDetailsResult = movieDetailsUseCase.getMovieDetails(movieId)
			when {
				movieDetailsResult.isOk -> _movieStateData.update {
					val movieDetail = movieDetailsResult.value
					it.copy(
						movieDetail = movieDetail,
						isRated = movieDetail.personalRating > -1f,
						isFavorite = movieDetail.favorite,
						isWatchlist = movieDetail.watchlist,
						failure = null
					)
				}

				else -> _movieStateData.update {
					it.copy(
						movieDetail = null,
						failure = movieDetailsResult.error
					)
				}
			}

		}
	}

	internal fun toastShown() {
		_movieStateData.update { it.copy(showRatingToast = false) }
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
	val isWatchlist: Boolean = false,
	val showRatingToast: Boolean = false,
)
