package com.zsoltbertalan.flickslate.account.data.network.model

import com.zsoltbertalan.flickslate.account.domain.model.FavoriteMovie
import com.zsoltbertalan.flickslate.shared.data.network.model.MovieDto
import com.zsoltbertalan.flickslate.shared.data.network.model.toMovie
import com.zsoltbertalan.flickslate.shared.domain.model.PageData
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import kotlinx.serialization.Serializable
import retrofit2.Response

@Suppress("PropertyName", "ConstructorParameterNaming")
@Serializable
internal data class FavoriteMovieReplyDto(
	val page: Int? = null,
	val total_pages: Int? = null,
	val total_results: Int? = null,
	val results: List<MovieDto>? = null,
)

internal fun List<MovieDto>.toFavoriteMovieList(): List<FavoriteMovie> =
	this.map { FavoriteMovie(it.toMovie()) }

internal fun Response<FavoriteMovieReplyDto>.toFavoriteMoviesReply(): PagingReply<FavoriteMovie> {
	val body = this.body()!!
	val etag = this.headers()["etag"] ?: ""
	val date = this.headers()["date"] ?: ""
	val expires = this.headers()["x-memc-expires"]?.toInt() ?: 0
	return PagingReply(
		pagingList = body.results?.toFavoriteMovieList() ?: emptyList(),
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
