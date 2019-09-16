package io.github.vnicius.twitterclone.data.repository.tweet

import android.app.Application
import io.github.vnicius.twitterclone.data.local.filemanage.SearchTweetsFileManager
import io.github.vnicius.twitterclone.utils.toFileName
import twitter4j.Query
import twitter4j.QueryResult
import twitter4j.Status

class TweetRepositoryLocal(myApp: Application) : TweetRepository {

    private val tweetFileManager = SearchTweetsFileManager(myApp)

    override suspend fun getTweetsByQueryAsync(query: Query, pageSize: Int): QueryResult? = null

    override suspend fun getTweetsAsync(queryText: String): List<Status>? {
        return tweetFileManager.getTweetsAsync(queryText.toFileName())
    }

    override suspend fun saveTweetsAsync(queryText: String, tweets: List<Status>): Boolean {
        return tweetFileManager.saveTweetsAsync(queryText.toFileName(), tweets)
    }

}