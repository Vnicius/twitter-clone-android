package io.github.vnicius.twitterclone.data.repository.tweet

import io.github.vnicius.twitterclone.data.remote.api.APIInterface
import io.github.vnicius.twitterclone.data.remote.api.TwitterAPI

/**
 * Implementation of the [TweetRepository] using the [APIInterface]
 */
class TweetRepositoryRemote : TweetRepository {

    private val mApi: APIInterface = TwitterAPI.instance    // API instance

    override suspend fun getTweetsByQueryAsync(query: String, count: Int) =
        mApi.searchAsync(query, count)
}
