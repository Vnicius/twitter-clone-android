package io.github.vnicius.twitterclone.ui.profile

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.github.vnicius.twitterclone.ui.common.BaseContract
import io.github.vnicius.twitterclone.utils.State
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
         */
        fun showTweets()
    }

    interface Presenter : BaseContract.Presenter {

        /**
         * Get the user information by the [userId]
         * @param [userId] the user id
         */
        fun getUser(userId: Long)

        fun buildTweets(userId: Long)

        fun getTweetsValue(): LiveData<PagedList<Status>>

        fun getTweetsState(): LiveData<State>
    }
}
