package io.github.vnicius.twitterclone.data.repository.tweet

import io.github.vnicius.twitterclone.api.APIInterface
import io.github.vnicius.twitterclone.api.TwitterAPI
import kotlinx.coroutines.Deferred
import twitter4j.Status

/**
 * Implementation of the [ITweetRepository] using the [APIInterface]
 */
class TweetRepository: ITweetRepository {

    // API instance
    private val mApi: APIInterface = TwitterAPI.instance

    override fun getTweetsByQuery(query: String, count: Int): Deferred<MutableList<Status>> = mApi.search(query, count)

}