package io.github.vnicius.twitterclone.data.repository.trends

import androidx.lifecycle.LiveData
import io.github.vnicius.twitterclone.data.model.Trend


/**
 * Interface to the repository to [Trend]
 */
interface TrendRepository {

    /**
     * Get the trends of a location by the [woeid]
     * @param [woeid] code of the location
     * @return a [Array] of [Trend]
     */
    suspend fun getTrendsAsync(woeid: Int): List<Trend>?

    suspend fun getTrendsLiveDataAsync(woeid: Int): LiveData<List<Trend>>

    suspend fun saveTrendsAsync(woeid: Int, trends: List<Trend>)
}
