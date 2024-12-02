package com.zsoltbertalan.flickslate.tv.data.network.model

import com.zsoltbertalan.flickslate.shared.model.PageData
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import com.zsoltbertalan.flickslate.tv.domain.model.TvShow
import kotlinx.serialization.Serializable
import retrofit2.Response

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class TopRatedTvReplyDto(
	val page: Int? = null,
	val total_pages: Int? = null,
	val total_results: Int? = null,
	val results: List<TvDto>? = null,
)

fun Response<TopRatedTvReplyDto>.toTvShowsReply(): PagingReply<TvShow> {
	val body = this.body()!!
	val etag = this.headers()["etag"] ?: ""
	val date = this.headers()["date"] ?: ""
	val expires = this.headers()["x-memc-expires"]?.toInt() ?: 0
	return PagingReply(
		pagingList = body.results?.toTvList() ?: emptyList(),
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
