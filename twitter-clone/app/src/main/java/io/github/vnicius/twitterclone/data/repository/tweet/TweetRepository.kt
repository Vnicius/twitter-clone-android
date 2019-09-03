package io.github.vnicius.twitterclone.data.repository.tweet

import kotlinx.coroutines.Deferred
import twitter4j.Status

/**
 * Interface to the repository to [Status] (Tweets)
 */
interface TweetRepository {

    /**
     * Search tweets by a [query] and limit by a [count]
     * @param [query] the query of the search
     * @param [count] maximum number of tweets
     * @return a async [MutableList] of [Status]
     */
    fun getTweetsByQueryAsync(query: String, count: Int): Deferred<MutableList<Status>>
}