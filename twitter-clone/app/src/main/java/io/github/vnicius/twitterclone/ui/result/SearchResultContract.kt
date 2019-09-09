package io.github.vnicius.twitterclone.ui.result

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import io.github.vnicius.twitterclone.data.datasource.searchtweets.SearchTweetsDataSource
import io.github.vnicius.twitterclone.ui.common.BaseContract
import io.github.vnicius.twitterclone.utils.State
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
        fun showResult()

        /**
         * Show the message that has no result to the query
         */
        fun showNoResult()

        fun showConnectionErrorMessage()
    }

    interface Presenter {

        /**
         * Search the tweets for a query
         */
        fun build(query: String)

        fun getValue(): LiveData<PagedList<Status>>

        fun getState(): LiveData<State>

        fun getDataSourceValue(): SearchTweetsDataSource?
    }
}
