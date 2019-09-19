package io.github.vnicius.twitterclone.data.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import io.github.vnicius.twitterclone.data.model.UserStatus

@Dao
interface UserStatusDao {
    @Query("SELECT * FROM user, status WHERE user.userId = :userId AND status.userOwnerId = userId ORDER BY status.createdAt DESC")
    fun getUserStatus(userId: Long): DataSource.Factory<Int, UserStatus>
}