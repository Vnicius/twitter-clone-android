package io.github.vnicius.twitterclone.data.remote.api

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
     * @return a async [MutableList] of [Status]
     */
    fun search(query: String, count: Int): Deferred<MutableList<Status>>

    /**
     * Get the trends of a location by the [woeid]
     * @param [woeid] code of the location
     * @return a async [Array] of [Trend]
     */
    fun getTrends(woeid: Int): Deferred<Array<Trend>>

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
    fun getUserTweest(userId: Long, count: Int): Deferred<ResponseList<Status>>
}