package com.zsoltbertalan.flickslate.account.data.network.model

import com.zsoltbertalan.flickslate.account.domain.model.FavoriteTvShow
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import kotlinx.serialization.Serializable
import retrofit2.Response

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
internal data class FavoriteTvShowReplyDto(
	val page: Int? = null,
	val total_pages: Int? = null,
	val total_results: Int? = null,
	val results: List<TvShow>? = null,
)

internal fun List<TvShow>.toFavoriteTvShowList(): List<FavoriteTvShow> =
	this.map { FavoriteTvShow(it) }

internal fun Response<FavoriteTvShowReplyDto>.toFavoriteTvShowsReply(): PagingReply<FavoriteTvShow> {
	val body = this.body()!!
	val etag = this.headers()["etag"] ?: ""
	val date = this.headers()["date"] ?: ""
	val expires = this.headers()["x-memc-expires"]?.toInt() ?: 0
	return PagingReply(
		pagingList = body.results?.toFavoriteTvShowList() ?: emptyList(),
		isLastPage = body.page == body.total_pages,
		pageData = PageData(
			body.page ?: 0,
			date,
			expires,
			etag,
			body.total_pages ?: 0,
			body.total_results ?: 0
		)
	)
}
