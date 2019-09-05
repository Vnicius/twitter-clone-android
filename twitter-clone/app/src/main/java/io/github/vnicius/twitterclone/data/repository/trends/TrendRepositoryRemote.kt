package io.github.vnicius.twitterclone.data.repository.trends

import io.github.vnicius.twitterclone.data.remote.api.APIInterface
import io.github.vnicius.twitterclone.data.remote.api.TwitterAPI

/**
 * Implementation of the [TrendRepository] using the [APIInterface]
 */
class TrendRepositoryRemote : TrendRepository {

    private val mApi: APIInterface = TwitterAPI.instance    // API instance

    override suspend fun getTrendsAsync(woeid: Int) = mApi.getTrendsAsync(woeid)
}
