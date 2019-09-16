package io.github.vnicius.twitterclone.data.datasource.usertweets

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.github.vnicius.twitterclone.data.repository.Repository
import io.github.vnicius.twitterclone.data.repository.user.UserRepository
import io.github.vnicius.twitterclone.utils.LogTagsUtils
import io.github.vnicius.twitterclone.utils.State
import kotlinx.coroutines.*
import twitter4j.Status
import twitter4j.TwitterException
import java.lang.Exception

class UserTweetsDataSource(
    private val userId: Long,
    private val pageSize: Int,
    private val userRepository: Repository<UserRepository>
) : PageKeyedDataSource<Int, Status>() {

    private val userTweetsDataSourceJob = SupervisorJob()
    private val userTweetsDataSourceScope = CoroutineScope(Dispatchers.IO + userTweetsDataSourceJob)
    var state: MutableLiveData<State> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Status>
    ) {
        state.postValue(State.LOADING)

        userTweetsDataSourceScope.launch {
            try {
                val result = userRepository.remote.getUserTweetsAsync(userId, pageSize, 1)

                result?.let {
                    val nextPage: Int? = if (it.isEmpty()) null else 2

                    callback.onResult(it, null, nextPage)
                    state.postValue(State.DONE)
                    userRepository.local.saveUserTweetsAsync(userId, it)
                }
            } catch (e: TwitterException) {
                Log.e(LogTagsUtils.DEBUG_EXCEPTION, "Twitter connection exception", e)

                state.postValue(State.CONNECTION_ERROR)
            } catch (e: Exception) {
                Log.e(LogTagsUtils.DEBUG_EXCEPTION, "Unknown exception", e)

                state.postValue(State.ERROR)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Status>) {
        userTweetsDataSourceScope.launch {
            try {
                val result = userRepository.remote.getUserTweetsAsync(userId, pageSize, params.key)

                result?.let {
                    val nextPage: Int? = if (it.isEmpty()) null else params.key + 1

                    callback.onResult(it, nextPage)
                }
            } catch (e: Exception) {
                state.postValue(State.ERROR)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Status>) {
        userTweetsDataSourceScope.launch {
            try {
                val result =
                    userRepository.remote.getUserTweetsAsync(userId, pageSize, params.key - 1)

                result?.let {
                    val previousPage: Int? =
                        if (it.isEmpty() || params.key - 2 == -1) null else params.key - 2

                    callback.onResult(it, previousPage)
                }
            } catch (e: Exception) {
                state.postValue(State.ERROR)
            }
        }
    }

    override fun invalidate() {
        super.invalidate()
        userTweetsDataSourceScope.coroutineContext.cancelChildren()
    }
}