package io.github.vnicius.twitterclone.data.repository.trends

import io.github.vnicius.twitterclone.api.APIInterface
import io.github.vnicius.twitterclone.api.TwitterAPI
import kotlinx.coroutines.Deferred
import twitter4j.Trend

class TrendRepository: ITrendRepository {
    private val mApi: APIInterface = TwitterAPI()

    override fun getTrends(woeid: Int): Deferred<Array<Trend>> = mApi.getTrends(woeid)
}