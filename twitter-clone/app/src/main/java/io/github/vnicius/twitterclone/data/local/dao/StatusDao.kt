package io.github.vnicius.twitterclone.data.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.vnicius.twitterclone.data.model.Status

@Dao
interface StatusDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(status: List<Status>)

    @Query("SELECT * FROM status WHERE userOwnerId = :userId ORDER BY createdAt DESC")
    fun getPaged(userId: Long): DataSource.Factory<Int, Status>

    @Query("SELECT * FROM status WHERE userOwnerId = :userId ORDER BY createdAt DESC")
    suspend fun getAll(userId: Long): List<Status>
}