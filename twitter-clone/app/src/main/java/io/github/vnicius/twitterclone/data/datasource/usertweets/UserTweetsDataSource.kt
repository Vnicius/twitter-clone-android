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
    private var currentPage = 1
    var state: MutableLiveData<State> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Status>
    ) {
        setStateValue(State.LOADING)

        userTweetsDataSourceScope.launch {
            try {
                val result = userRepository.getUserTweetsAsync(userId, pageSize, currentPage)
                callback.onResult(result.toMutableList(), null, ++currentPage)
                setStateValue(State.DONE)
            } catch (e: Exception) {
                setStateValue(State.ERROR)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Status>) {
        userTweetsDataSourceScope.launch {
            try {
                val result = userRepository.getUserTweetsAsync(userId, pageSize, currentPage)
                callback.onResult(result.toMutableList(), ++currentPage)
            } catch (e: Exception) {
                setStateValue(State.ERROR)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Status>) {
        userTweetsDataSourceScope.launch {
            try {
                val result = userRepository.getUserTweetsAsync(userId, pageSize, --currentPage)
                callback.onResult(result.toMutableList(), --currentPage)
            } catch (e: Exception) {
                setStateValue(State.ERROR)
            }
        }
    }

    override fun invalidate() {
        super.invalidate()
        userTweetsDataSourceScope.coroutineContext.cancelChildren()
    }

    private fun setStateValue(value: State) {
        state.postValue(value)
    }
}