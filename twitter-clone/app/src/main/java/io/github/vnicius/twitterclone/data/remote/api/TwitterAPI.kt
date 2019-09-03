package io.github.vnicius.twitterclone.data.remote.api

import io.github.vnicius.twitterclone.BuildConfig
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import twitter4j.*
import twitter4j.conf.Configuration
import twitter4j.conf.ConfigurationBuilder

/**
 * Class to access the Twitter API using Twitter4j
 */
class TwitterAPI: APIInterface {

    private var twitterInstance: Twitter

    init {
        val config = createConfiguration()
        val twitterFactory = TwitterFactory(config)
        twitterInstance = twitterFactory.instance
    }

    companion object {
        val instance: APIInterface = TwitterAPI()
    }

    /**
     * Create the configuration of the Twitter4j
     */
    private fun createConfiguration(): Configuration {
        val configBuilder = ConfigurationBuilder()
        configBuilder.setDebugEnabled(true)
            .setOAuthConsumerKey(BuildConfig.TWITTER_CONSUMER_KEY)
            .setOAuthConsumerSecret(BuildConfig.TWITTER_CONSUMER_SECRET)
            .setOAuthAccessToken(BuildConfig.TWITTER_ACCESS_TOKEN)
            .setOAuthAccessTokenSecret(BuildConfig.TWITTER_ACCESS_TOKEN_SECRET)
            .setTweetModeExtended(true)

        return configBuilder.build()
    }

    override fun searchAsync(query: String, count: Int) = GlobalScope.async {
            val querySearch = Query(query).count(count)
            val result = twitterInstance.search(querySearch)
            result.tweets
        }

    override fun getTrendsAsync(woeid: Int) = GlobalScope.async {
        val trends = twitterInstance.getPlaceTrends(woeid)
        trends.trends
    }

    override fun getUserAsync(userId: Long) = GlobalScope.async {
        twitterInstance.showUser(userId)
    }

    override fun getUserTweetsAsync(userId: Long, count: Int) = GlobalScope.async {
        twitterInstance.getUserTimeline(userId, Paging(1, count))
    }

}