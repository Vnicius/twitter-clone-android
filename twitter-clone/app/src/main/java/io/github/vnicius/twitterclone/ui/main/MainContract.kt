package io.github.vnicius.twitterclone.ui.main

import io.github.vnicius.twitterclone.ui.common.BaseContract
import twitter4j.Trend

/**
 * Contract for View and Presenter for the Main UI
 */
class MainContract {

    interface View : BaseContract.View {

        /**
         * Show a list of trends
         * @param [trends] a [Array] of [Trend]
         */
        fun showTrends(trends: Array<Trend>)

        fun showConnectionErrorMessage()
    }

    interface Presenter : BaseContract.Presenter {

        /**
         * Get the list of trends
         */
        fun getTrends()
    }
}