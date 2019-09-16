package io.github.vnicius.twitterclone.data.repository.tweet

import twitter4j.Query
import twitter4j.QueryResult
import twitter4j.Status

/**
 * Interface to the repository to [Status] (Tweets)
 */
interface TweetRepository {

    /**
     * Search tweets by a [query] and limit by a [count]
     * @param [query] the query of the search
     * @param [count] maximum number of tweets
     * @return [MutableList] of [Status]
     */
    suspend fun getTweetsByQueryAsync(
        query: Query,
        pageSize: Int
    ): QueryResult?

    suspend fun getTweetsAsync(queryText: String): List<Status>?

    suspend fun saveTweetsAsync(queryText: String, tweets: List<Status>): Boolean
}
