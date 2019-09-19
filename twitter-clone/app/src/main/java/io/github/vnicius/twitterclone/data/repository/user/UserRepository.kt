package io.github.vnicius.twitterclone.data.repository.user

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import io.github.vnicius.twitterclone.data.model.Status
import io.github.vnicius.twitterclone.data.model.User
import io.github.vnicius.twitterclone.data.model.UserStatus
import twitter4j.ResponseList

/**
 * Interface to the repository to [User]
 */
interface UserRepository {

    /**
     * Get the user information by the [userId]
     * @param [userId] the id of the user
     * @return [User] object
     */
    suspend fun getUserAsync(userId: Long): User?

    suspend fun getUserLiveDataAsync(userId: Long): LiveData<User>

    /**
     * Get the tweets of a specific user
     * @param [userId] the user id
     * @param [count] the maximum number of tweets
     * @return [ResponseList] of [Status]
     */
    suspend fun getUserTweetsAsync(userId: Long, pageSize: Int, page: Int = 1): List<Status>?

    fun getUserTweetsPaged(userId: Long): DataSource.Factory<Int, UserStatus>?

    suspend fun saveUserTweetsAsync(tweets: List<Status>)

    suspend fun saveUserAsync(user: User)
}
