package com.zsoltbertalan.flickslate.shared.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Genre(val id: Int? = null, val name: String? = null) : Parcelable
