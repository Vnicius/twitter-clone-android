package io.github.vnicius.twitterclone.data.datasource.usertweets

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import io.github.vnicius.twitterclone.data.repository.user.UserRepository
import io.github.vnicius.twitterclone.utils.State
import kotlinx.coroutines.*
import twitter4j.Status
import java.lang.Exception

class UserTweetsDataSource(
    private val userId: Long,
    private val pageSize: Int,
    private val userRepository: UserRepository
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
                val result = userRepository.getUserTweetsAsync(userId, pageSize, 1)
                var nextPage: Int? = 2

                if (result.isEmpty()) {
                    nextPage = null
                }

                callback.onResult(result.toMutableList(), null, nextPage)
                state.postValue(State.DONE)
            } catch (e: Exception) {
                state.postValue(State.ERROR)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Status>) {
        userTweetsDataSourceScope.launch {
            try {
                val result = userRepository.getUserTweetsAsync(userId, pageSize, params.key)
                var nextPage: Int? = params.key + 1

                if (result.isEmpty()) {
                    nextPage = null
                }

                callback.onResult(result.toMutableList(), nextPage)
            } catch (e: Exception) {
                state.postValue(State.ERROR)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Status>) {
        userTweetsDataSourceScope.launch {
            try {
                val result = userRepository.getUserTweetsAsync(userId, pageSize, params.key - 1)
                var nextPage: Int? = params.key - 2

                if (result.isEmpty() || nextPage == -1) {
                    nextPage = null
                }

                callback.onResult(result.toMutableList(), nextPage)
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