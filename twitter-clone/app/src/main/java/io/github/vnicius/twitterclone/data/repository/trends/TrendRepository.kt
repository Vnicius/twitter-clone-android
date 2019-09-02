package io.github.vnicius.twitterclone.data.repository.trends

import io.github.vnicius.twitterclone.data.remote.api.APIInterface
import io.github.vnicius.twitterclone.data.remote.api.TwitterAPI
import kotlinx.coroutines.Deferred
import twitter4j.Trend

/**
 * Implementation of the [ITrendRepository] using the [APIInterface]
 */
class TrendRepository: ITrendRepository {

    // API instance
    private val mApi: APIInterface = TwitterAPI.instance

    override fun getTrends(woeid: Int): Deferred<Array<Trend>> = mApi.getTrends(woeid)
}