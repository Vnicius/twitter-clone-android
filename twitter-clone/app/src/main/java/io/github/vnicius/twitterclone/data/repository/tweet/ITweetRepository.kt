package io.github.vnicius.twitterclone.data.repository.tweet

import kotlinx.coroutines.Deferred
import twitter4j.Status

interface ITweetRepository {
    fun getTweetsByQuery(query: String, count: Int): Deferred<MutableList<Status>>
}