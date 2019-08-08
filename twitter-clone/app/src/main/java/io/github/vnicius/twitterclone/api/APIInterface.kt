package io.github.vnicius.twitterclone.api

import io.github.vnicius.twitterclone.data.model.Tweet
import io.github.vnicius.twitterclone.data.model.User
import kotlinx.coroutines.Deferred
import twitter4j.Status

interface APIInterface {
    fun search(query: String): Deferred<MutableList<Status>>
    fun getUser(username: String): User
}