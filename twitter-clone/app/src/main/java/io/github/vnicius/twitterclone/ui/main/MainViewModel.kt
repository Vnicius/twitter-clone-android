package io.github.vnicius.twitterclone.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vnicius.twitterclone.data.repository.trends.TrendRepository
import io.github.vnicius.twitterclone.data.repository.trends.TrendRepositoryRemote
import io.github.vnicius.twitterclone.utils.LogTagsUtils
import io.github.vnicius.twitterclone.utils.State
import kotlinx.coroutines.*
import twitter4j.Trend
import twitter4j.TwitterException
import java.lang.Exception

/**
 * Main ViewModel
 * @property view the instance of the view
 */
class MainViewModel : ViewModel() {

    private val trendRepository: TrendRepository = TrendRepositoryRemote()
    var trends: MutableLiveData<Array<Trend>> = MutableLiveData()
    var state: MutableLiveData<State> = MutableLiveData()

    fun getTrends() {
        state.postValue(State.LOADING)

        viewModelScope.launch {
            try {
                trends.postValue(trendRepository.getTrendsAsync(1))
                state.postValue(State.DONE)
            } catch (e: TwitterException) {
                Log.e(LogTagsUtils.DEBUG_EXCEPTION, "Twitter connection exception", e)

                state.postValue(State.CONNECTION_ERROR)
            } catch (e: Exception) {
                Log.e(LogTagsUtils.DEBUG_EXCEPTION, "Unknown exception", e)

                state.postValue(State.ERROR)
            }
        }
    }
}
