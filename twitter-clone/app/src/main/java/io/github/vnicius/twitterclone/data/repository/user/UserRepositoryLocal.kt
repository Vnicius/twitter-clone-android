package io.github.vnicius.twitterclone.data.repository.user

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import io.github.vnicius.twitterclone.data.local.database.AppDatabase
import io.github.vnicius.twitterclone.data.model.User
import twitter4j.Status

class UserRepositoryLocal(val myApp: Application) : UserRepository {

    private val userDao = AppDatabase.getInstance(myApp.applicationContext).userDao()

    override suspend fun getUserAsync(userId: Long): User? = userDao.getById(userId).value

    override suspend fun getUserTweetsAsync(
        userId: Long,
        pageSize: Int,
        page: Int
    ): List<Status>? = listOf()

    override suspend fun saveUserTweetsAsync(userId: Long, tweets: List<Status>) {}

    override suspend fun saveUserAsync(user: User) = userDao.insert(user)

    override suspend fun getUserLiveDataAsync(userId: Long): LiveData<User> =
        userDao.getById(userId)
}