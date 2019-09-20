package io.github.vnicius.twitterclone.data.repository.tweet

import android.app.Application
import twitter4j.Query
import twitter4j.QueryResult
import twitter4j.Status

class TweetRepositoryLocal(myApp: Application) : TweetRepository {

    override suspend fun getTweetsByQueryAsync(query: Query, pageSize: Int): QueryResult? = null

    override suspend fun getTweetsAsync(queryText: String): List<Status>? = null

    override suspend fun saveTweetsAsync(queryText: String, tweets: List<Status>): Boolean = false
}