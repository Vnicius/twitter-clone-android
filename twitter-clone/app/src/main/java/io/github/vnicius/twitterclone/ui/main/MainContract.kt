package io.github.vnicius.twitterclone.ui.main

import io.github.vnicius.twitterclone.data.model.Tweet

class MainContract {

    interface View {
        fun showSearchMessage()
        fun showLoader()
        fun showResult(tweets: Tweet)
        fun showNoResult()
    }

    interface Presenter {
        fun searchTweets(query: String)
    }
}