package io.github.vnicius.twitterclone.ui.result

import io.github.vnicius.twitterclone.ui.common.BaseContract
import twitter4j.Status

/**
 * Contract for View and Presenter for the Profile UI
 */
interface SearchResultContract {

    interface View : BaseContract.View {
        /**
         * Show the tweets of the search result
         * @param [tweets] list o tweets
         */
        fun showResult(tweets: MutableList<Status>)

        /**
         * Show the message that has no result to the query
         */
        fun showNoResult()
    }

    interface Presenter : BaseContract.Presenter {

        /**
         * Search the tweets for a query
         */
        fun searchTweets(query: String)
    }
}