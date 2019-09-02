package io.github.vnicius.twitterclone.data.repository.user

import kotlinx.coroutines.Deferred
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
     * @return a async [User] object
     */
    fun getUser(userId: Long): Deferred<User>

    /**
     * Get the tweets of a specific user
     * @param [userId] the user id
     * @param [count] the maximum number of tweets
     * @return a async [ResponseList] of [Status]
     */
    fun getUserTweets(userId: Long, count: Int): Deferred<ResponseList<Status>>
}