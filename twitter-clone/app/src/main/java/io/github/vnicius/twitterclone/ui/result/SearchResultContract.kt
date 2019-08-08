package io.github.vnicius.twitterclone.ui.result

import twitter4j.Status

interface SearchResultContract {
    interface View {
        fun showLoader()
        fun showResult(tweets: MutableList<Status>)
        fun showNoResult()
    }

    interface Presenter {
        fun searchTweets(query: String)
    }
}