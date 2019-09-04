package io.github.vnicius.twitterclone.data.repository.trends

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import twitter4j.Trend

/**
 * Interface to the repository to [Trend]
 */
interface TrendRepository {

    /**
     * Get the trends of a location by the [woeid]
     * @param [woeid] code of the location
     * @return a [Array] of [Trend]
     */
    suspend fun getTrendsAsync(woeid: Int): Array<Trend>
}