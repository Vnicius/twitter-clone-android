package io.github.vnicius.twitterclone.data.repository.user

import kotlinx.coroutines.Deferred
import twitter4j.ResponseList
import twitter4j.Status
import twitter4j.User

interface IUserRepository {
    fun getUser(userId: Long): Deferred<User>
    fun getUserTweets(userId: Long, count: Int): Deferred<ResponseList<Status>>
}