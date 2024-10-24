package com.zsoltbertalan.flickslate.search.repository

import com.zsoltbertalan.flickslate.movies.domain.model.Movie
import com.zsoltbertalan.flickslate.search.data.api.GenreDataSource
import com.zsoltbertalan.flickslate.search.data.api.GenreMoviesDataSource
import com.zsoltbertalan.flickslate.search.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.shared.async.IoDispatcher
import com.zsoltbertalan.flickslate.shared.data.getresult.fetchCacheThenRemote
import com.zsoltbertalan.flickslate.shared.domain.model.GenresReply
import com.zsoltbertalan.flickslate.shared.domain.model.PagingReply
import com.zsoltbertalan.flickslate.shared.util.Outcome
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreAccessor @Inject constructor(
	private val genreDataSource: GenreDataSource.Local,
	private val genreRemoteDataSource: GenreDataSource.Remote,
	private val genreMoviesDataSource: GenreMoviesDataSource.Local,
	private val genreMoviesRemoteDataSource: GenreMoviesDataSource.Remote,
	@IoDispatcher val dispatcher: CoroutineDispatcher
) : GenreRepository {

	override fun getGenresList(): Flow<Outcome<GenresReply>> {
		return fetchCacheThenRemote(
			fetchFromLocal = { genreDataSource.getGenres() },
			makeNetworkRequest = {
				val etag = genreDataSource.getEtag()
				genreRemoteDataSource.getGenres(etag = etag)
			},
			saveResponseData = { genresReply ->
				val etag = genresReply.etag ?: ""
				genreDataSource.insertEtag(etag)
				genreDataSource.insertGenres(genresReply.genres.orEmpty())
			},
		).flowOn(dispatcher)
	}

	override fun getGenreDetail(genreId: Int, page: Int): Flow<Outcome<PagingReply<Movie>>> {
		return fetchCacheThenRemote(
			fetchFromLocal = { genreMoviesDataSource.getGenreMovies(page) },
			makeNetworkRequest = { genreMoviesRemoteDataSource.getGenreMovies(genreId = genreId, page = page) },
			saveResponseData = { pagingReply ->
				val moviesReply = pagingReply.pagingList
				genreMoviesDataSource.insertGenreMoviesPageData(
					pagingReply.pageData
				)
				genreMoviesDataSource.insertGenreMovies(moviesReply, page)
			},
		)
	}

}
