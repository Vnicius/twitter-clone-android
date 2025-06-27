package io.github.vnicius.twitterclone.data.remote.api

import twitter4j.*


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
    suspend fun searchAsync(query: Query, pageSize: Int): QueryResult

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
    suspend fun getUserTweetsAsync(userId: Long, pageSize: Int, page: Int = 1): List<Status>

    suspend fun getLocations(): List<Location>
}
