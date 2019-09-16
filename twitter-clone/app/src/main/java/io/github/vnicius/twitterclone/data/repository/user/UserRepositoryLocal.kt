package io.github.vnicius.twitterclone.data.repository.user

import android.app.Application
import io.github.vnicius.twitterclone.data.local.filemanage.UserFileManager
import twitter4j.ResponseList
import twitter4j.Status
import twitter4j.User

class UserRepositoryLocal(val myApp: Application) : UserRepository {

    private val userFileManager = UserFileManager(myApp)

    override suspend fun getUserAsync(userId: Long): User? = userFileManager.getUserAsync(userId)

    override suspend fun getUserTweetsAsync(
        userId: Long,
        pageSize: Int,
        page: Int
    ): List<Status>? = userFileManager.getUserTweetsAsync(userId)

    override suspend fun saveUserTweetsAsync(userId: Long, tweets: List<Status>): Boolean {
        return userFileManager.saveUserTweets(userId, tweets)
    }

    override suspend fun saveUserAsync(user: User): Boolean {
        return userFileManager.saveUser(user)
    }
}