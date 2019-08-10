package io.github.vnicius.twitterclone.data.repository.tweet

import kotlinx.coroutines.Deferred
import twitter4j.Status

/**
 * Interface to the repository to [Status] (Tweets)
 */
interface ITweetRepository {

    /**
     * Search tweets by a [query] and limit by a [count]
     * @param [query] the query of the search
     * @param [count] maximum number of tweets
     * @return a async [MutableList] of [Status]
     */
    fun getTweetsByQuery(query: String, count: Int): Deferred<MutableList<Status>>
}