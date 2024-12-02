package com.zsoltbertalan.flickslate.shared.compose.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.shared.model.MovieCardType

fun Modifier.navigate(id: Int?, popTo: (Int) -> Unit) = this.clickable {
	id?.let { popTo(it) }
}

fun Modifier.movieCardWidth(type: MovieCardType) =
	if (type == MovieCardType.FULL) this.fillMaxWidth() else this.width(300.dp)
