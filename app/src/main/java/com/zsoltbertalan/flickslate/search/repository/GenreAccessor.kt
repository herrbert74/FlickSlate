package com.zsoltbertalan.flickslate.search.repository

import com.zsoltbertalan.flickslate.domain.model.Genre
import com.zsoltbertalan.flickslate.domain.model.PageData
import com.zsoltbertalan.flickslate.domain.model.PagingReply
import com.zsoltbertalan.flickslate.movies.data.network.model.MoviesReplyDto
import com.zsoltbertalan.flickslate.movies.data.network.model.toMoviesReply
import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.search.data.db.GenreDataSource
import com.zsoltbertalan.flickslate.search.data.db.GenreMoviesDataSource
import com.zsoltbertalan.flickslate.search.data.network.model.GenreReplyDto
import com.zsoltbertalan.flickslate.search.data.network.model.toGenres
import com.zsoltbertalan.flickslate.search.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.search.network.SearchService
import com.zsoltbertalan.flickslate.shared.async.IoDispatcher
import com.zsoltbertalan.flickslate.shared.data.getresult.fetchCacheThenNetworkResponse
import com.zsoltbertalan.flickslate.shared.util.Outcome
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreAccessor @Inject constructor(
	private val searchService: SearchService,
	private val genreDataSource: GenreDataSource,
	private val genreMoviesDataSource: GenreMoviesDataSource,
	@IoDispatcher val dispatcher: CoroutineDispatcher
) : GenreRepository {

	override fun getGenresList(): Flow<Outcome<List<Genre>>> {
		return fetchCacheThenNetworkResponse(
			fetchFromLocal = { genreDataSource.getGenres() },
			makeNetworkRequest = {
				val etag = genreDataSource.getEtag()
				searchService.getGenres(ifNoneMatch = etag)
			},
			saveResponseData = { genreResponse ->
				val etag = genreResponse.headers()["etag"] ?: ""
				genreDataSource.insertEtag(etag)
				val genres = genreResponse.body()?.toGenres().orEmpty()
				genreDataSource.insertGenres(genres)
			},
			mapper = GenreReplyDto::toGenres,
		).flowOn(dispatcher)
	}

	override fun getGenreDetail(genreId: Int, page: Int): Flow<Outcome<PagingReply<Movie>>> {
		return fetchCacheThenNetworkResponse(
			fetchFromLocal = { genreMoviesDataSource.getGenreMovies(page) },
			makeNetworkRequest = { searchService.getGenreMovie(withGenres = genreId, page = page) },
			saveResponseData = { response ->
				val etag = response.headers()["etag"] ?: ""
				val moviesReply = response.body()?.toMoviesReply()
				genreMoviesDataSource.insertGenreMoviesPageData(
					PageData(
						page,
						response.headers()["date"] ?: "",
						response.headers()["x-memc-expires"]?.toInt() ?: 0,
						etag,
						response.body()?.total_pages ?: 0,
						response.body()?.total_results ?: 0,
					)
				)
				genreMoviesDataSource.insertGenreMovies(moviesReply?.pagingList.orEmpty(), page)
			},
			mapper = MoviesReplyDto::toMoviesReply,
		)
	}

}
