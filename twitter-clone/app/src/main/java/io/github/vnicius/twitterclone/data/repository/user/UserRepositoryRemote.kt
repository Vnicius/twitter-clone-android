package io.github.vnicius.twitterclone.data.repository.user

import io.github.vnicius.twitterclone.data.remote.api.APIInterface
import io.github.vnicius.twitterclone.data.remote.api.TwitterAPI

/**
 * Implementation of the [UserRepository] using the [APIInterface]
 */
class UserRepositoryRemote : UserRepository {

    private val mApi: APIInterface = TwitterAPI.instance    // API instance

    override suspend fun getUserAsync(userId: Long) = mApi.getUserAsync(userId)

    override suspend fun getUserTweetsAsync(userId: Long, pageSize: Int, page: Int) =
        mApi.getUserTweetsAsync(userId, pageSize, page)
}
