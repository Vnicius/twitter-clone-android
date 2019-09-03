package io.github.vnicius.twitterclone.data.repository.user

import io.github.vnicius.twitterclone.data.remote.api.APIInterface
import io.github.vnicius.twitterclone.data.remote.api.TwitterAPI
import kotlinx.coroutines.Deferred
import twitter4j.ResponseList
import twitter4j.Status
import twitter4j.User

/**
 * Implementation of the [UserRepository] using the [APIInterface]
 */
class UserRepositoryRemote: UserRepository {

    // API instance
    private val mApi: APIInterface = TwitterAPI.instance

    override fun getUserAsync(userId: Long): Deferred<User> = mApi.getUserAsync(userId)
    override fun getUserTweetsAsync(userId: Long, count: Int): Deferred<ResponseList<Status>> = mApi.getUserTweetsAsync(userId, count)

}