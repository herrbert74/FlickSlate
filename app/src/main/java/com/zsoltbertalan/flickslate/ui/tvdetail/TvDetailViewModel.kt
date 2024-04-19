package com.zsoltbertalan.flickslate.ui.tvdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.flickslate.domain.api.TvRepository
import com.zsoltbertalan.flickslate.domain.model.TvDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val tvRepository: TvRepository
) : ViewModel() {

	private val _tvStateData = MutableStateFlow(TvDetail())
	val tvStateData = _tvStateData.asStateFlow()

	private val seriesId: Int = checkNotNull(savedStateHandle["seriesId"])

	init {
		getTvDetail()
	}

	private fun getTvDetail() {
		viewModelScope.launch {
			when (val response = tvRepository.getTvDetails(seriesId)) {
				is Ok -> {
					_tvStateData.value = response.value
				}

				else -> Unit // handle error
			}

		}
	}
}