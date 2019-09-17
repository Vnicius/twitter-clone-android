package io.github.vnicius.twitterclone.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import io.github.vnicius.twitterclone.data.model.Trend
import io.github.vnicius.twitterclone.data.repository.Repository
import io.github.vnicius.twitterclone.data.repository.RepositoryFactory
import io.github.vnicius.twitterclone.data.repository.trends.TrendRepository
import io.github.vnicius.twitterclone.utils.LogTagsUtils
import io.github.vnicius.twitterclone.utils.State
import kotlinx.coroutines.*
import twitter4j.TwitterException
import java.lang.Exception

/**
 * Main ViewModel
 * @property view the instance of the view
 */
class MainViewModel(myApplication: Application) : AndroidViewModel(myApplication) {

    private val trendRepository: Repository<TrendRepository> =
        RepositoryFactory.createRepository<TrendRepository>()?.create(myApplication) as Repository<TrendRepository>
    var trends: LiveData<List<Trend>>? = null
    var state: MutableLiveData<State> = MutableLiveData()

    fun getLocalTrends(): Job {
        state.postValue(State.LOADING)
        return viewModelScope.launch {
            trends = trendRepository.local.getTrendsLiveDataAsync(1)
        }
    }

    fun getTrends() {
        viewModelScope.launch {
            try {
                val data = trendRepository.remote.getTrendsAsync(1)

                if (data != null) {
                    trendRepository.local.saveTrendsAsync(1, data)
                }

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
