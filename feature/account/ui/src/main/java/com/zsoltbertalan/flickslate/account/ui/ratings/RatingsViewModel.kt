package com.zsoltbertalan.flickslate.account.ui.ratings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.coroutines.coroutineBinding
import com.github.michaelbull.result.fold
import com.zsoltbertalan.flickslate.account.domain.model.RatedMovie
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvEpisode
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvShow
import com.zsoltbertalan.flickslate.account.domain.usecase.GetRatedMoviesUseCase
import com.zsoltbertalan.flickslate.account.domain.usecase.GetRatedTvShowEpisodesUseCase
import com.zsoltbertalan.flickslate.account.domain.usecase.GetRatedTvShowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatingsViewModel @Inject constructor(
	private val getRatedMoviesUseCase: GetRatedMoviesUseCase,
	private val getRatedTvShowsUseCase: GetRatedTvShowsUseCase,
	private val getRatedTvShowEpisodesUseCase: GetRatedTvShowEpisodesUseCase,
) : ViewModel() {

	private val _uiState = MutableStateFlow<RatingsUiState>(RatingsUiState.Loading)
	val uiState = _uiState.asStateFlow()

	init {
		fetchRatings()
	}

	private fun fetchRatings() {
		viewModelScope.launch {
			_uiState.value = RatingsUiState.Loading

			val result = coroutineBinding {
				val movies = async { getRatedMoviesUseCase.execute(page = 1).bind() }
				val tvShows = async { getRatedTvShowsUseCase.execute(page = 1).bind() }
				val episodes = async { getRatedTvShowEpisodesUseCase.execute(page = 1).bind() }
				Triple(movies.await(), tvShows.await(), episodes.await())
			}

			result.fold(
				success = {
					_uiState.value = RatingsUiState.Success(
						ratedMovies = it.first.pagingList.toImmutableList(),
						ratedTvShows = it.second.pagingList.toImmutableList(),
						ratedTvEpisodes = it.third.pagingList.toImmutableList()
					)
				},
				failure = {
					_uiState.value = RatingsUiState.Error(it.message)
				}
			)
		}
	}
}

sealed class RatingsUiState {
	data object Loading : RatingsUiState()
	data class Success(
		val ratedMovies: ImmutableList<RatedMovie> = listOf<RatedMovie>().toImmutableList(),
		val ratedTvShows: ImmutableList<RatedTvShow> = listOf<RatedTvShow>().toImmutableList(),
		val ratedTvEpisodes: ImmutableList<RatedTvEpisode> = listOf<RatedTvEpisode>().toImmutableList()
	) : RatingsUiState()

	data class Error(val message: String) : RatingsUiState()
}
