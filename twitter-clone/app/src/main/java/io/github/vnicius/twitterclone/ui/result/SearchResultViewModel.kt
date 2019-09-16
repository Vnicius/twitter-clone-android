package io.github.vnicius.twitterclone.ui.result

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.github.vnicius.twitterclone.data.datasource.searchtweets.SearchTweetsDataSource
import io.github.vnicius.twitterclone.data.datasource.searchtweets.SearchTweetsDataSourceFactory
import io.github.vnicius.twitterclone.data.repository.Repository
import io.github.vnicius.twitterclone.data.repository.RepositoryFactory
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepository
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepositoryRemote
import io.github.vnicius.twitterclone.utils.State
import twitter4j.Status

private const val MAX_PAGES = 5
private const val MAX_ITEMS = 10

/**
 * SearchResult Presenter
 */
class SearchResultViewModel(myApp: Application) : AndroidViewModel(myApp) {

    // repository instance
    private val tweetRepository: Repository<TweetRepository> =
        RepositoryFactory.createRepository<TweetRepository>()?.create(myApp) as Repository<TweetRepository>
    private lateinit var searchTweetsDataSourceFactory: SearchTweetsDataSourceFactory
    lateinit var tweetsList: LiveData<PagedList<Status>>
    lateinit var state: LiveData<State>


    fun build(query: String) {
        searchTweetsDataSourceFactory =
            SearchTweetsDataSourceFactory(query, MAX_ITEMS, tweetRepository)
        val config = PagedList.Config.Builder()
            .setPageSize(MAX_PAGES)
            .setInitialLoadSizeHint(MAX_PAGES * 2)
            .setEnablePlaceholders(false)
            .build()
        tweetsList = LivePagedListBuilder(searchTweetsDataSourceFactory, config).build()
        state = Transformations.switchMap(
            searchTweetsDataSourceFactory.searchTweetsDataSourceLiveData,
            SearchTweetsDataSource::state
        )
    }

    fun getDataSourceValue(): SearchTweetsDataSource? =
        searchTweetsDataSourceFactory.searchTweetsDataSourceLiveData.value
}
