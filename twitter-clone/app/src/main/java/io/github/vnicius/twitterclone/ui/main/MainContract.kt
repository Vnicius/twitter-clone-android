package io.github.vnicius.twitterclone.ui.main

import twitter4j.Trend

/**
 * Contract for View and Presenter for the Main UI
 */
class MainContract {

    interface View {

        /**
         * Show a loader to the user
         */
        fun showLoader()

        /**
         * Show a list of trends
         * @param [trends] a [Array] of [Trend]
         */
        fun showTrends(trends: Array<Trend>)

        /**
         * Show a error message to the user
         * @param message with the error
         */
        fun showError(message: String)
    }

    interface Presenter {

        /**
         * Get the list of trends
         */
        fun getTrends()
    }
}