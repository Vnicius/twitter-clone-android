package io.github.vnicius.twitterclone.data.repository.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.github.vnicius.twitterclone.data.model.Status
import io.github.vnicius.twitterclone.data.model.User
import io.github.vnicius.twitterclone.data.model.UserStatus
import io.github.vnicius.twitterclone.data.remote.api.APIInterface
import io.github.vnicius.twitterclone.data.remote.api.TwitterAPI

/**
 * Implementation of the [UserRepository] using the [APIInterface]
 */
class UserRepositoryRemote : UserRepository {

    private val mApi: APIInterface = TwitterAPI.instance    // API instance

    override suspend fun getUserAsync(userId: Long) =
        User.ModelMapper.from(mApi.getUserAsync(userId))

    override suspend fun getUserTweetsAsync(userId: Long, pageSize: Int, page: Int) =
        mApi.getUserTweetsAsync(userId, pageSize, page).map { status ->
            Status.ModelMapper.from(
                status
            )
        }

    override suspend fun saveUserTweetsAsync(tweets: List<Status>) {}

    override suspend fun saveUserAsync(user: User) {}

    override suspend fun getUserLiveDataAsync(userId: Long): LiveData<User> =
        MutableLiveData<User>()

    override fun getUserTweetsPaged(userId: Long): DataSource.Factory<Int, UserStatus>? = null
}
