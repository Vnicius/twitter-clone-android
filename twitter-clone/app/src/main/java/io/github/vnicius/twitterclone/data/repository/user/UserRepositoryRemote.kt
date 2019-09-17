package io.github.vnicius.twitterclone.data.repository.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.vnicius.twitterclone.data.model.User
import io.github.vnicius.twitterclone.data.remote.api.APIInterface
import io.github.vnicius.twitterclone.data.remote.api.TwitterAPI
import twitter4j.Status

/**
 * Implementation of the [UserRepository] using the [APIInterface]
 */
class UserRepositoryRemote : UserRepository {

    private val mApi: APIInterface = TwitterAPI.instance    // API instance

    override suspend fun getUserAsync(userId: Long) =
        User.ModelMapper.from(mApi.getUserAsync(userId))

    override suspend fun getUserTweetsAsync(userId: Long, pageSize: Int, page: Int) =
        mApi.getUserTweetsAsync(userId, pageSize, page)

    override suspend fun saveUserTweetsAsync(userId: Long, tweets: List<Status>) {}

    override suspend fun saveUserAsync(user: User) {}

    override suspend fun getUserLiveDataAsync(userId: Long): LiveData<User> =
        MutableLiveData<User>()
}
