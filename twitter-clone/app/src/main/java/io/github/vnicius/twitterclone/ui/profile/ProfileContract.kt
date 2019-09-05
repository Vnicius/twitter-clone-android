package io.github.vnicius.twitterclone.ui.profile

import io.github.vnicius.twitterclone.ui.common.BaseContract
import twitter4j.Status
import twitter4j.User

/**
 * Contract for View and Presenter for the Profile UI
 */
interface ProfileContract {

    interface View : BaseContract.View {

        /**
         * Show the [user] information
         * @param [user] a [User] object
         */
        fun showUser(user: User)

        /**
         * Show the tweets of the user
         * @param [tweets] a list os [Status] (tweets)
         */
        fun showTweets(tweets: MutableList<Status>)
    }

    interface Presenter : BaseContract.Presenter {

        /**
         * Get the user information by the [userId]
         * @param [userId] the user id
         */
        fun getUser(userId: Long)

        /**
         * Get the tweets of the user by the [userId]
         * @param [userId] the user id
         */
        fun getHomeTweets(userId: Long)
    }
}
