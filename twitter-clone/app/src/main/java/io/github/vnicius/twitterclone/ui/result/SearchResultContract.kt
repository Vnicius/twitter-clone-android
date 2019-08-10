package io.github.vnicius.twitterclone.ui.result

import twitter4j.Status

/**
 * Contract for View and Presenter for the Profile UI
 */
interface SearchResultContract {

    interface View {
        /**
         * Show a loader to the user
         */
        fun showLoader()

        /**
         * Show the tweets of the search result
         * @param [tweets] list o tweets
         */
        fun showResult(tweets: MutableList<Status>)

        /**
         * Show the message that has no result to the query
         */
        fun showNoResult()

        /**
         * Show a error message to the user
         * @param message with the error
         */
        fun showError(message: String)
    }

    interface Presenter {

        /**
         * Search the tweets for a query
         */
        fun searchTweets(query: String)
    }
}