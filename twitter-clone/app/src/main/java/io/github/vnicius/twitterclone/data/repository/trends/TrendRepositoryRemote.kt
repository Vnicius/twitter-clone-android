package io.github.vnicius.twitterclone.data.repository.trends

import io.github.vnicius.twitterclone.data.remote.api.APIInterface
import io.github.vnicius.twitterclone.data.remote.api.TwitterAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import twitter4j.Trend

/**
 * Implementation of the [TrendRepository] using the [APIInterface]
 */
class TrendRepositoryRemote : TrendRepository {

    // API instance
    private val mApi: APIInterface = TwitterAPI.instance

    override suspend fun getTrendsAsync(woeid: Int) = mApi.getTrendsAsync(woeid)
}