package io.github.vnicius.twitterclone.data.repository.user

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import io.github.vnicius.twitterclone.data.local.database.AppDatabase
import io.github.vnicius.twitterclone.data.model.Status
import io.github.vnicius.twitterclone.data.model.User
import io.github.vnicius.twitterclone.data.model.UserStatus

class UserRepositoryLocal(val myApp: Application) : UserRepository {

    private val appDatabase = AppDatabase.getInstance(myApp.applicationContext)
    private val userDao = appDatabase.userDao()
    private val statusDao = appDatabase.statusDao()
    private val userStatusDao = appDatabase.userStatusDao()

    override suspend fun getUserAsync(userId: Long): User? = userDao.getById(userId).value

    override suspend fun getUserTweetsAsync(
        userId: Long,
        pageSize: Int,
        page: Int
    ): List<Status>? = statusDao.getAll(userId)

    override suspend fun saveUserTweetsAsync(tweets: List<Status>) = statusDao.insertAll(tweets)

    override suspend fun saveUserAsync(user: User) = userDao.insert(user)

    override suspend fun getUserLiveDataAsync(userId: Long): LiveData<User> =
        userDao.getById(userId)

    override fun getUserTweetsPaged(userId: Long): DataSource.Factory<Int, UserStatus> =
        userStatusDao.getUserStatus(userId)
}