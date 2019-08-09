package io.github.vnicius.twitterclone.ui.main

import twitter4j.Trend

class MainContract {

    interface View {
        fun showLoader()
        fun showTrends(trends: Array<Trend>)
        fun showError()
    }

    interface Presenter {
        fun getTrends()
    }
}