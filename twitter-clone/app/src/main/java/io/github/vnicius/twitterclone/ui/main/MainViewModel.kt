package io.github.vnicius.twitterclone.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vnicius.twitterclone.data.repository.trends.TrendRepository
import io.github.vnicius.twitterclone.data.repository.trends.TrendRepositoryRemote
import io.github.vnicius.twitterclone.utils.LogTagsUtils
import kotlinx.coroutines.*
import twitter4j.Trend
import twitter4j.TwitterException

/**
 * Main Presenter
 * @property view the instance of the view
 */
class MainViewModel() : ViewModel() {

    private val trendRepository: TrendRepository = TrendRepositoryRemote()
    var trends: MutableLiveData<Array<Trend>> = MutableLiveData()

    fun getTrends() {
        viewModelScope.launch {
            trends.postValue(trendRepository.getTrendsAsync(1))
        }
    }
}
