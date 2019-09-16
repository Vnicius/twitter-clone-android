package io.github.vnicius.twitterclone.data.local.filemanage

import android.app.Application
import twitter4j.Status
import twitter4j.User

class UserFileManager(private val myApp: Application) {

    private val fileManager: FileManager = FileManager(myApp)

    suspend fun getUserAsync(userId: Long): User? {
        return fileManager.readData("${USER_FILE_BASE_NAME}$userId") as User?
    }

    suspend fun getUserTweetsAsync(userId: Long): List<Status>? {
        return fileManager.readData("${TWEETS_FILE_BASE_NAME}$userId") as List<Status>?
    }

    suspend fun saveUser(user: User): Boolean {
        return fileManager.saveData("${USER_FILE_BASE_NAME}${user.id}", user)
    }

    suspend fun saveUserTweets(userId: Long, tweets: List<Status>): Boolean {
        return fileManager.saveData("${TWEETS_FILE_BASE_NAME}$userId", tweets)
    }

    companion object {
        const val USER_FILE_BASE_NAME = "user_"
        const val TWEETS_FILE_BASE_NAME = "user_home_"
    }
}