package io.github.vnicius.twitterclone.data.repository.tweet

import io.github.vnicius.twitterclone.data.remote.api.APIInterface
import io.github.vnicius.twitterclone.data.remote.api.TwitterAPI
import twitter4j.Query
import twitter4j.Status

/**
 * Implementation of the [TweetRepository] using the [APIInterface]
 */
class TweetRepositoryRemote : TweetRepository {

    private val mApi: APIInterface = TwitterAPI.instance    // API instance

    override suspend fun getTweetsByQueryAsync(query: Query, pageSize: Int) =
        mApi.searchAsync(query, pageSize)

    override suspend fun getTweetsAsync(queryText: String): List<Status> = listOf()

    override suspend fun saveTweetsAsync(queryText: String, tweets: List<Status>): Boolean = false
}
