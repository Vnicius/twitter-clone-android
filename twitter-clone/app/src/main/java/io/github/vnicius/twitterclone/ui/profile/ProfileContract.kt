package io.github.vnicius.twitterclone.ui.profile

import twitter4j.Status
import twitter4j.User

interface ProfileContract {
    interface View {
        fun showUser(user: User)
        fun showTweets(tweets: MutableList<Status>)
        fun showLoader()
        fun showError(message: String)
    }

    interface Presenter {
        fun getUser(userId: Long)
        fun getHomeTweets(userId: Long)
    }
}