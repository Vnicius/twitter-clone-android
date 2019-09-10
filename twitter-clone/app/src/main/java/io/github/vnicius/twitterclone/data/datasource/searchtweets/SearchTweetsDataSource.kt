package io.github.vnicius.twitterclone.data.datasource.searchtweets

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import android.util.Log
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepository
import io.github.vnicius.twitterclone.utils.LogTagsUtils
import io.github.vnicius.twitterclone.utils.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import twitter4j.Query
import twitter4j.Status
import twitter4j.TwitterException

class SearchTweetsDataSource(
    val queryText: String,
    val pageSize: Int,
    val tweetsRepository: TweetRepository
) : PageKeyedDataSource<Query, Status>() {

    private val tweetsDataSourceJob = SupervisorJob()
    private val tweetsDataSourceScope = CoroutineScope(Dispatchers.IO + tweetsDataSourceJob)
    var state: MutableLiveData<State> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Query>,
        callback: LoadInitialCallback<Query, Status>
    ) {
        setStateValue(State.LOADING)

        tweetsDataSourceScope.launch {
            try {
                val result = tweetsRepository.getTweetsByQueryAsync(queryText, pageSize)
                callback.onResult(result.tweets, null, result.nextQuery())

                if (result.tweets.isEmpty()) {
                    setStateValue(State.NO_RESULT)
                } else {
                    setStateValue(State.DONE)
                }
            } catch (e: TwitterException) {
                Log.e(LogTagsUtils.DEBUG_EXCEPTION, "Twitter connection exception", e)

                setStateValue(State.ERROR)
            } catch (e: Exception) {
                Log.e(LogTagsUtils.DEBUG_EXCEPTION, "Unknown exception", e)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Query>, callback: LoadCallback<Query, Status>) {
        tweetsDataSourceScope.launch {
            try {
                val result = tweetsRepository.getTweetsByQueryAsync(queryText, pageSize)
                callback.onResult(result.tweets, result.nextQuery())
            } catch (e: TwitterException) {
                Log.e(LogTagsUtils.DEBUG_EXCEPTION, "Twitter connection exception", e)

                setStateValue(State.ERROR)
            } catch (e: Exception) {
                Log.e(LogTagsUtils.DEBUG_EXCEPTION, "Unknown exception", e)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Query>, callback: LoadCallback<Query, Status>) {
    }

    private fun setStateValue(value: State) {
        state.postValue(value)
    }
}