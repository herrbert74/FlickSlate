package com.zsoltbertalan.flickslate.search.data.repository

import com.zsoltbertalan.flickslate.base.kotlin.async.IoDispatcher
import com.zsoltbertalan.flickslate.base.kotlin.result.Outcome
import com.zsoltbertalan.flickslate.search.data.api.GenreDataSource
import com.zsoltbertalan.flickslate.search.data.api.GenreMoviesDataSource
import com.zsoltbertalan.flickslate.search.domain.api.GenreRepository
import com.zsoltbertalan.flickslate.search.domain.api.model.GenreMoviesPagingReply
import com.zsoltbertalan.flickslate.shared.data.getresult.fetchCacheThenRemote
import com.zsoltbertalan.flickslate.shared.domain.model.GenresReply
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class GenreAccessor internal constructor(
	private val genreDataSource: GenreDataSource.Local,
	private val genreRemoteDataSource: GenreDataSource.Remote,
	private val genreMoviesDataSource: GenreMoviesDataSource.Local,
	private val genreMoviesRemoteDataSource: GenreMoviesDataSource.Remote,
	@param:IoDispatcher val dispatcher: CoroutineDispatcher
) : GenreRepository {

	override fun getGenresList(): Flow<Outcome<GenresReply>> {
		return fetchCacheThenRemote(
			fetchFromLocal = { genreDataSource.getGenres() },
			makeNetworkRequest = {
				val etag = genreDataSource.getEtag() ?: ""
				genreRemoteDataSource.getGenres(etag = etag)
			},
			saveResponseData = { genresReply ->
				val etag = genresReply.etag ?: ""
				genreDataSource.insertEtag(etag)
				genreDataSource.insertGenres(genresReply.genres.orEmpty())
			},
		).flowOn(dispatcher)
	}

	override fun getGenreDetail(genreId: Int, page: Int): Flow<Outcome<GenreMoviesPagingReply>> {
		return fetchCacheThenRemote(
			fetchFromLocal = { genreMoviesDataSource.getGenreMovies(genreId, page) },
			makeNetworkRequest = {
				val etag = genreMoviesDataSource.getEtag(genreId, page)
				genreMoviesRemoteDataSource.getGenreMovies(etag, genreId = genreId, page = page)
			},
			saveResponseData = { genrePagingReply ->
				val moviesReply = genrePagingReply.pagingReply.pagingList
				genreMoviesDataSource.insertGenreMoviesPageData(
					genreId,
					genrePagingReply.pagingReply.pageData
				)
				genreMoviesDataSource.insertGenreMovies(genreId, moviesReply, page)
			},
		)
	}

}
