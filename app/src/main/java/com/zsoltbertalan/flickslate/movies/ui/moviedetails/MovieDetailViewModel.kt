package com.zsoltbertalan.flickslate.movies.ui.moviedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsoltbertalan.flickslate.movies.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.shared.domain.model.Failure
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val moviesRepository: MoviesRepository
) : ViewModel() {

	private val _movieStateData = MutableStateFlow(MovieDetailState())
	val movieStateData = _movieStateData.asStateFlow()

	private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

	init {
		getMovieDetail()
	}

	private fun getMovieDetail() {
		viewModelScope.launch {
			val movieDetailsResult = moviesRepository.getMovieDetails(movieId)
			when {
				movieDetailsResult.isOk -> _movieStateData.update {
					it.copy(
						movieDetail = movieDetailsResult.value,
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
}

data class MovieDetailState(
	val movieDetail: MovieDetail? = null,
	val failure: Failure? = null,
)
