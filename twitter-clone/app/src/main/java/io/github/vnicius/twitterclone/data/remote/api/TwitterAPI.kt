package io.github.vnicius.twitterclone.data.remote.api

import io.github.vnicius.twitterclone.BuildConfig
import kotlinx.coroutines.*
import twitter4j.*
import twitter4j.conf.ConfigurationBuilder

/**
 * Class to access the Twitter API using Twitter4j
 */
class TwitterAPI : APIInterface {

    private var twitterInstance: Twitter = TwitterFactory(createConfiguration()).instance

    override suspend fun searchAsync(query: String, count: Int): MutableList<Status> =
        withContext(Dispatchers.IO) {
            val querySearch = Query(query).count(count)
            val result = twitterInstance.search(querySearch)

            result.tweets
        }

    override suspend fun getTrendsAsync(woeid: Int): Array<Trend> = withContext(Dispatchers.IO) {
        val trends = twitterInstance.getPlaceTrends(woeid)

        trends.trends
    }

    override suspend fun getUserAsync(userId: Long): User =
        withContext(Dispatchers.IO) { twitterInstance.showUser(userId) }

    override suspend fun getUserTweetsAsync(userId: Long, count: Int): ResponseList<Status> =
        withContext(Dispatchers.IO) {
            twitterInstance.getUserTimeline(userId, Paging(1, count))
        }

    /**
     * Create the configuration of the Twitter4j
     */
    private fun createConfiguration() = ConfigurationBuilder().apply {
        setDaemonEnabled(true)
        setOAuthConsumerKey(BuildConfig.TWITTER_CONSUMER_KEY)
        setOAuthConsumerSecret(BuildConfig.TWITTER_CONSUMER_SECRET)
        setOAuthAccessToken(BuildConfig.TWITTER_ACCESS_TOKEN)
        setOAuthAccessTokenSecret(BuildConfig.TWITTER_ACCESS_TOKEN_SECRET)
        setTweetModeExtended(true)
    }.build()

    companion object {
        val instance: APIInterface = TwitterAPI()
    }
}
