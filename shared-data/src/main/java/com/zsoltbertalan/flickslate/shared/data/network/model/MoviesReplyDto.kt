package com.zsoltbertalan.flickslate.shared.data.network.model

import com.zsoltbertalan.flickslate.shared.model.Movie
import com.zsoltbertalan.flickslate.shared.model.PageData
import com.zsoltbertalan.flickslate.shared.model.PagingReply
import kotlinx.serialization.Serializable
import retrofit2.Response

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class MoviesReplyDto(
	val page: Int? = null,
	val total_pages: Int? = null,
	val total_results: Int? = null,
	val results: List<MovieDto>? = null,
)

fun MoviesReplyDto.toMoviesReply() =
	PagingReply(
		this.results?.toMovieList() ?: emptyList(),
		page == total_pages,
		pageData = PageData(
			this.page ?: 0,
			"",
			0,
			"",
			this.total_pages ?: 0,
			this.total_results ?: 0
		)
	)

fun Response<MoviesReplyDto>.toMoviesReply(): PagingReply<Movie> {
	val body = this.body()!!
	val etag = this.headers()["etag"] ?: ""
	val date = this.headers()["date"] ?: ""
	val expires = this.headers()["x-memc-expires"]?.toInt() ?: 0
	return PagingReply(
		pagingList = body.results?.toMovieList() ?: emptyList(),
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
