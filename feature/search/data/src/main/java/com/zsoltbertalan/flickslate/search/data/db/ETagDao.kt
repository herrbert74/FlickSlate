package com.zsoltbertalan.flickslate.search.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.zsoltbertalan.flickslate.search.data.db.model.EtagEntity

@Dao
internal interface ETagDao {

	@Query("SELECT * FROM etags")
	fun getAll(): List<EtagEntity>

	@Upsert
	fun insertEtag(eTag: EtagEntity)

	@Query("DELETE FROM etags")
	fun purgeDatabase()

}
