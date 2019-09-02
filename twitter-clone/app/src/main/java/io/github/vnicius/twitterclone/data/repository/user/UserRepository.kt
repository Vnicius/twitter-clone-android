package io.github.vnicius.twitterclone.data.repository.user

import io.github.vnicius.twitterclone.data.remote.api.APIInterface
import io.github.vnicius.twitterclone.data.remote.api.TwitterAPI
import kotlinx.coroutines.Deferred
import twitter4j.ResponseList
import twitter4j.Status
import twitter4j.User

/**
 * Implementation of the [IUserRepository] using the [APIInterface]
 */
class UserRepository: IUserRepository {

    // API instance
    private val mApi: APIInterface = TwitterAPI.instance

    override fun getUser(userId: Long): Deferred<User> = mApi.getUser(userId)
    override fun getUserTweets(userId: Long, count: Int): Deferred<ResponseList<Status>> = mApi.getUserTweest(userId, count)

}