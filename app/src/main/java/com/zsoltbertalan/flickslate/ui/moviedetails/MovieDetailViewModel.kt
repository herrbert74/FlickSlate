package com.zsoltbertalan.flickslate.ui.moviedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.domain.model.MovieDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val moviesRepository: MoviesRepository
) : ViewModel() {

	private val _movieStateData = MutableStateFlow(MovieDetail())
	val movieStateData = _movieStateData.asStateFlow()

	private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

	init {
		getMovieDetail()
	}

	private fun getMovieDetail() {
		viewModelScope.launch {
			when (val response = moviesRepository.getMovieDetails(movieId)) {
				is Ok -> {
					_movieStateData.value = response.value
				}

				else -> Unit // handle error
			}
		}
	}
}
