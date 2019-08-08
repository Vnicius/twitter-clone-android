package io.github.vnicius.twitterclone.api

import io.github.vnicius.twitterclone.BuildConfig
import io.github.vnicius.twitterclone.data.model.Tweet
import io.github.vnicius.twitterclone.data.model.User
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import twitter4j.Query
import twitter4j.Status
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.conf.Configuration
import twitter4j.conf.ConfigurationBuilder

class TwitterAPI: APIInterface {


    private final lateinit var twitterInstance: Twitter

    init {
        val config = createConfiguration()
        val twitterFactory = TwitterFactory(config)
        twitterInstance = twitterFactory.instance
    }

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

    override fun search(query: String) = GlobalScope.async {
            val querySearch = Query(query).count(10)
            val result = twitterInstance.search(querySearch)
            result.tweets
        }

    override fun getUser(username: String): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}