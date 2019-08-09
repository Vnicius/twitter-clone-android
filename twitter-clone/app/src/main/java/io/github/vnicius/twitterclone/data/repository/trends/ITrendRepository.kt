package io.github.vnicius.twitterclone.data.repository.trends

import kotlinx.coroutines.Deferred
import twitter4j.Trend

interface ITrendRepository {

    fun getTrends(woeid: Int): Deferred<Array<Trend>>
}