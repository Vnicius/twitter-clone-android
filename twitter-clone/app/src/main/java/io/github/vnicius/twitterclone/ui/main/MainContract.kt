package io.github.vnicius.twitterclone.ui.main

import io.github.vnicius.twitterclone.data.model.Tweet
import kotlinx.coroutines.CoroutineScope
import twitter4j.Status

class MainContract {

    interface View {
        fun showSearchMessage()
        fun showLoader()
        fun showResult(tweets: MutableList<Status>)
        fun showNoResult()
    }

    interface Presenter {
        fun searchTweets(query: String)
    }
}