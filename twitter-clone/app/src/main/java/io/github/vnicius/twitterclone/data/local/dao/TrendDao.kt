package io.github.vnicius.twitterclone.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.vnicius.twitterclone.data.model.Trend

@Dao
interface TrendDao {
    @Query("SELECT * FROM trend ORDER BY id")
    fun getAll(): LiveData<List<Trend>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(trend: List<Trend>)
}