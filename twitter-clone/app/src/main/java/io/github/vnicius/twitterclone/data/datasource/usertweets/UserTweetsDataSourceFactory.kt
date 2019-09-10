package io.github.vnicius.twitterclone.data.datasource.usertweets

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import io.github.vnicius.twitterclone.data.repository.user.UserRepository
import twitter4j.Status

class UserTweetsDataSourceFactory(
    private val userId: Long,
    private val pageSize: Int,
    private val userRepository: UserRepository
) : DataSource.Factory<Int, Status>() {

    val userTweetsDataSourceLiveData = MutableLiveData<UserTweetsDataSource>()

    override fun create(): DataSource<Int, Status> {
        val userTweetsDataSource = UserTweetsDataSource(userId, pageSize, userRepository)
        userTweetsDataSourceLiveData.postValue(userTweetsDataSource)
        return userTweetsDataSource
    }
}