package io.github.vnicius.twitterclone.data.repository.tweet

import io.github.vnicius.twitterclone.api.APIInterface
import io.github.vnicius.twitterclone.api.TwitterAPI
import kotlinx.coroutines.Deferred
import twitter4j.Status

class TweetRepository: ITweetRepository {

    private val mApi: APIInterface = TwitterAPI()

    override fun getTweetsByQuery(query: String, count: Int): Deferred<MutableList<Status>> = mApi.search(query, count)

}