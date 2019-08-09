package io.github.vnicius.twitterclone.api

import kotlinx.coroutines.Deferred
import twitter4j.Status
import twitter4j.Trend

interface APIInterface {
    fun search(query: String, count: Int): Deferred<MutableList<Status>>
    fun getTrends(woeid: Int): Deferred<Array<Trend>>
    fun getUser(username: String): Unit
}