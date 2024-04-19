package com.zsoltbertalan.flickslate.ui.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.zsoltbertalan.flickslate.domain.api.TvRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvViewModel @Inject constructor(tvRepository: TvRepository) : ViewModel() {

	val topRatedTvList = tvRepository.getTopRatedTv("en").cachedIn(viewModelScope)

}
