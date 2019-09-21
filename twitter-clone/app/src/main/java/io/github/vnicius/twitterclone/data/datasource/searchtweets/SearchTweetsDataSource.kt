package io.github.vnicius.twitterclone.data.datasource.searchtweets

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import android.util.Log
import io.github.vnicius.twitterclone.data.model.UserStatus
import io.github.vnicius.twitterclone.data.repository.RepositoryFactory
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepository
import io.github.vnicius.twitterclone.utils.LogTagsUtils
import io.github.vnicius.twitterclone.utils.State
import kotlinx.coroutines.*
import twitter4j.Query
import twitter4j.Status
import twitter4j.TwitterException

class SearchTweetsDataSource(
    val queryText: String,
    val pageSize: Int,
    val tweetsRepository: RepositoryFactory<TweetRepository>
) : PageKeyedDataSource<Query, UserStatus>() {

    private val tweetsDataSourceJob = SupervisorJob()
    private val tweetsDataSourceScope = CoroutineScope(Dispatchers.IO + tweetsDataSourceJob)
    var state: MutableLiveData<State> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Query>,
        callback: LoadInitialCallback<Query, UserStatus>
    ) {
        state.postValue(State.LOADING)

        tweetsDataSourceScope.launch {
            try {
                val result =
                    tweetsRepository.getRemote().getTweetsByQueryAsync(Query(queryText), pageSize)

                if (result != null) {
                    callback.onResult(result.tweets.map {
                        UserStatus.ModelMapper.from(
                            it.user,
                            it
                        )
                    }, null, result.nextQuery())

                    if (result.tweets.isEmpty()) {
                        state.postValue(State.NO_RESULT)
                    } else {
                        state.postValue(State.DONE)
                    }
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

    override fun loadAfter(params: LoadParams<Query>, callback: LoadCallback<Query, UserStatus>) {
        tweetsDataSourceScope.launch {
            try {
                val result =
                    tweetsRepository.getRemote().getTweetsByQueryAsync(params.key, pageSize)
                if (result != null) {
                    callback.onResult(result.tweets.map {
                        UserStatus.ModelMapper.from(
                            it.user,
                            it
                        )
                    }, result.nextQuery())
                }
            } catch (e: TwitterException) {
                Log.e(LogTagsUtils.DEBUG_EXCEPTION, "Twitter connection exception", e)

                state.postValue(State.ERROR)
            } catch (e: Exception) {
                Log.e(LogTagsUtils.DEBUG_EXCEPTION, "Unknown exception", e)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Query>, callback: LoadCallback<Query, UserStatus>) {
    }

    override fun invalidate() {
        super.invalidate()
        tweetsDataSourceScope.coroutineContext.cancelChildren()
    }
}