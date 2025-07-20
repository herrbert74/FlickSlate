package com.zsoltbertalan.flickslate.shared.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class GenresReply(val genres: List<Genre>? = null, val etag: String? = null) : Parcelable
