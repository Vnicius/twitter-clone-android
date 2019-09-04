package io.github.vnicius.twitterclone.data.remote.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import twitter4j.ResponseList
import twitter4j.Status
import twitter4j.Trend
import twitter4j.User

/**
 * Interface to the Twitter API calls
 */
interface APIInterface {

    /**
     * Search tweets by a [query] and limit by a [count]
     * @param [query] the query of the search
     * @param [count] maximum number of tweets
     * @return a [MutableList] of [Status]
     */
    suspend fun searchAsync(query: String, count: Int): MutableList<Status>

    /**
     * Get the trends of a location by the [woeid]
     * @param [woeid] code of the location
     * @return a [Array] of [Trend]
     */
    suspend fun getTrendsAsync(woeid: Int): Array<Trend>

    /**
     * Get the user information by the [userId]
     * @param [userId] the id of the user
     * @return a [User] object
     */
    suspend fun getUserAsync(userId: Long): User

    /**
     * Get the tweets of a specific user
     * @param [userId] the user id
     * @param [count] the maximum number of tweets
     * @return a [ResponseList] of [Status]
     */
    suspend fun getUserTweetsAsync(userId: Long, count: Int): ResponseList<Status>
}