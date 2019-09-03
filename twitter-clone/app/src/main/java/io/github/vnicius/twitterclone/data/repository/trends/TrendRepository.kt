package io.github.vnicius.twitterclone.data.repository.trends

import kotlinx.coroutines.Deferred
import twitter4j.Trend

/**
 * Interface to the repository to [Trend]
 */
interface TrendRepository {

    /**
     * Get the trends of a location by the [woeid]
     * @param [woeid] code of the location
     * @return a async [Array] of [Trend]
     */
    fun getTrendsAsync(woeid: Int): Deferred<Array<Trend>>
}