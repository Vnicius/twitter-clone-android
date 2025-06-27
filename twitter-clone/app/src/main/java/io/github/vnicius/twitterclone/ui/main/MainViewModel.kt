package io.github.vnicius.twitterclone.ui.main

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.data.model.Trend
import io.github.vnicius.twitterclone.data.repository.RepositoryFactory
import io.github.vnicius.twitterclone.data.repository.trends.TrendRepository
import io.github.vnicius.twitterclone.utils.LogTagsUtils
import io.github.vnicius.twitterclone.utils.SharedPreferencesKeys
import io.github.vnicius.twitterclone.utils.State
import kotlinx.coroutines.*
import twitter4j.TwitterException
import java.lang.Exception

/**
 * Main ViewModel
 * @property view the instance of the view
 */
class MainViewModel(val myApplication: Application) : AndroidViewModel(myApplication) {

    private val trendRepository: RepositoryFactory<TrendRepository>? =
        RepositoryFactory.createRepository<TrendRepository>(myApplication) as RepositoryFactory<TrendRepository>?
    val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(myApplication.applicationContext)
    var locationWoeid: Int = getWoeid()
    var trends: LiveData<List<Trend>>? = null
    var state: MutableLiveData<State> = MutableLiveData()

    fun getLocalTrends(): Job {
        state.postValue(State.LOADING)
        return viewModelScope.launch {
            if (trendRepository != null) {
                trends = trendRepository.getLocal().getTrendsLiveDataAsync(locationWoeid)
            }
        }
    }

    fun getTrends() {
        viewModelScope.launch {
            try {
                if (trendRepository != null) {
                    val data = trendRepository.getRemote().getTrendsAsync(locationWoeid)

                    if (data != null) {
                        trendRepository.getLocal().saveTrendsAsync(locationWoeid, data)
                    }

                    state.postValue(State.DONE)
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

    private fun getWoeid(): Int = sharedPreferences.getInt(SharedPreferencesKeys.WOEID, 1)

    fun getLocationName(): String? = sharedPreferences.getString(
        SharedPreferencesKeys.LOCATION_NAME, myApplication.getString(
            R.string.label_worldwide
        )
    )

    fun updateLocation() {
        locationWoeid = getWoeid()
    }

}
