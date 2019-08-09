package io.github.vnicius.twitterclone.ui.result

import twitter4j.Status

interface SearchResultContract {
    interface View {
        fun showLoader()
        fun showResult(tweets: MutableList<Status>)
        fun showNoResult()
        fun showError()
    }

    interface Presenter {
        fun searchTweets(query: String)
    }
}