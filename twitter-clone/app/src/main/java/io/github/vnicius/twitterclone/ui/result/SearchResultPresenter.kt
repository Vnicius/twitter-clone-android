package io.github.vnicius.twitterclone.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.github.vnicius.twitterclone.data.datasource.searchtweets.SearchTweetsDataSource
import io.github.vnicius.twitterclone.data.datasource.searchtweets.SearchTweetsDataSourceFactory
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepository
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepositoryRemote
import io.github.vnicius.twitterclone.utils.State
import twitter4j.Status

private const val MAX_PAGES = 5
private const val MAX_ITEMS = 10

/**
 * SearchResult Presenter
 */
class SearchResultPresenter : SearchResultContract.Presenter {

    // repository instance
    private val tweetRepository: TweetRepository = TweetRepositoryRemote()
    private lateinit var tweetsList: LiveData<PagedList<Status>>
    private lateinit var searchTweetsDataSourceFactory: SearchTweetsDataSourceFactory

    override fun build(query: String) {
        searchTweetsDataSourceFactory =
            SearchTweetsDataSourceFactory(query, MAX_ITEMS, tweetRepository)
        val config = PagedList.Config.Builder()
            .setPageSize(MAX_PAGES)
            .setInitialLoadSizeHint(MAX_PAGES * 2)
            .setEnablePlaceholders(false)
            .build()
        tweetsList = LivePagedListBuilder(searchTweetsDataSourceFactory, config).build()
    }

    override fun getValue() = tweetsList

    override fun getState(): LiveData<State> = Transformations.switchMap(
        searchTweetsDataSourceFactory.searchTweetsDataSourceLiveData,
        SearchTweetsDataSource::state
    )

    override fun getDataSourceValue(): SearchTweetsDataSource? =
        searchTweetsDataSourceFactory.searchTweetsDataSourceLiveData.value
}
