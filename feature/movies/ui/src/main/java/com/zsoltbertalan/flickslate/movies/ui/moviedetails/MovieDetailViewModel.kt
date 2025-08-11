package com.zsoltbertalan.flickslate.movies.ui.moviedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsoltbertalan.flickslate.movies.domain.model.MovieDetailWithImages
import com.zsoltbertalan.flickslate.movies.domain.usecase.MovieDetailsUseCase
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
	private val movieDetailsUseCase: MovieDetailsUseCase
) : ViewModel() {

	private val _movieStateData = MutableStateFlow(MovieDetailState())
	val movieStateData = _movieStateData.asStateFlow()

	private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

	init {
		getMovieDetail()
	}

	private fun getMovieDetail() {
		viewModelScope.launch {
			val movieDetailsResult = movieDetailsUseCase.getMovieDetails(movieId)
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
	val movieDetail: MovieDetailWithImages? = null,
	val failure: Failure? = null,
)
