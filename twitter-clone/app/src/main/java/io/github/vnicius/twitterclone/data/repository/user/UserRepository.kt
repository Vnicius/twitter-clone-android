package io.github.vnicius.twitterclone.data.repository.user

import twitter4j.ResponseList
import twitter4j.Status
import twitter4j.User

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

    /**
     * Get the tweets of a specific user
     * @param [userId] the user id
     * @param [count] the maximum number of tweets
     * @return [ResponseList] of [Status]
     */
    suspend fun getUserTweetsAsync(userId: Long, pageSize: Int, page: Int = 1): List<Status>?

    suspend fun saveUserTweetsAsync(userId: Long, tweets: List<Status>): Boolean

    suspend fun saveUserAsync(user: User): Boolean
}
