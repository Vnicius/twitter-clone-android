package io.github.vnicius.twitterclone.data.repository.tweet

import io.github.vnicius.twitterclone.data.remote.api.APIInterface
import io.github.vnicius.twitterclone.data.remote.api.TwitterAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import twitter4j.Status

/**
 * Implementation of the [TweetRepository] using the [APIInterface]
 */
class TweetRepositoryRemote : TweetRepository {

    // API instance
    private val mApi: APIInterface = TwitterAPI.instance

    override suspend fun getTweetsByQueryAsync(query: String, count: Int) =
        mApi.searchAsync(query, count)

}