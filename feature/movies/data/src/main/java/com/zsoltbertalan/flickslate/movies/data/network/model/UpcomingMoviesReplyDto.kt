package com.zsoltbertalan.flickslate.movies.data.network.model

import com.zsoltbertalan.flickslate.shared.data.network.model.MovieDto
import com.zsoltbertalan.flickslate.shared.data.network.model.toMovieList
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import kotlinx.serialization.Serializable
import retrofit2.Response

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
data class UpcomingMoviesReplyDto(
	val page: Int? = null,
	val total_pages: Int? = null,
	val total_results: Int? = null,
	val results: List<MovieDto>? = null,
	val dates: DatesDto? = null,
)

fun Response<UpcomingMoviesReplyDto>.toMoviesReply(): PagingReply<Movie> {
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
