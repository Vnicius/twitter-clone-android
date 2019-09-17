package io.github.vnicius.twitterclone.data.repository.trends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.vnicius.twitterclone.data.model.Trend
import io.github.vnicius.twitterclone.data.remote.api.APIInterface
import io.github.vnicius.twitterclone.data.remote.api.TwitterAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of the [TrendRepository] using the [APIInterface]
 */
class TrendRepositoryRemote : TrendRepository {

    private val mApi: APIInterface = TwitterAPI.instance    // API instance

    override suspend fun getTrendsAsync(woeid: Int): List<Trend>? =
        mApi.getTrendsAsync(woeid).toList().mapIndexed { index, trend ->
            Trend(index, trend.name, trend.query, trend.tweetVolume)
        }

    override suspend fun saveTrendsAsync(woeid: Int, trends: List<Trend>) {}

    override suspend fun getTrendsLiveDataAsync(woeid: Int): LiveData<List<Trend>> =
        withContext(Dispatchers.IO) {
            MutableLiveData<List<Trend>>().apply {
                postValue(mApi.getTrendsAsync(woeid).toList().mapIndexed { index, trend ->
                    Trend(index, trend.name, trend.query, trend.tweetVolume)
                })
            }
        }
}
