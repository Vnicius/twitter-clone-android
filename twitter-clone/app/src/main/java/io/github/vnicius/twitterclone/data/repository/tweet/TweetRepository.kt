package io.github.vnicius.twitterclone.data.repository.tweet

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
    suspend fun getTweetsByQueryAsync(query: String, count: Int): MutableList<Status>
}
