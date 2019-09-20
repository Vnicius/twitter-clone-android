package io.github.vnicius.twitterclone.ui.result

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.github.vnicius.twitterclone.data.datasource.searchtweets.SearchTweetsDataSource
import io.github.vnicius.twitterclone.data.datasource.searchtweets.SearchTweetsDataSourceFactory
import io.github.vnicius.twitterclone.data.repository.RepositoryFactory
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepository
import io.github.vnicius.twitterclone.utils.State
import kotlinx.coroutines.launch
import twitter4j.Status


/**
 * SearchResult Presenter
 */
class SearchResultViewModel(myApp: Application) : AndroidViewModel(myApp) {

    // repository instance
    private val tweetRepository: RepositoryFactory<TweetRepository>? =
        RepositoryFactory.createRepository<TweetRepository>(myApp) as RepositoryFactory<TweetRepository>?
    private lateinit var searchTweetsDataSourceFactory: SearchTweetsDataSourceFactory
    lateinit var state: LiveData<State>
    lateinit var tweetsList: LiveData<PagedList<Status>>

    fun build(query: String) {
        if (tweetRepository != null) {
            searchTweetsDataSourceFactory =
                SearchTweetsDataSourceFactory(query, PAGE_SIZE, tweetRepository)
            val config = PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)
                .setInitialLoadSizeHint(PAGE_SIZE * 2)
                .setEnablePlaceholders(false)
                .build()
            tweetsList = LivePagedListBuilder(searchTweetsDataSourceFactory, config).build()
            state = Transformations.switchMap(
                searchTweetsDataSourceFactory.searchTweetsDataSourceLiveData,
                SearchTweetsDataSource::state
            )
        }
    }

    fun getDataSourceValue(): SearchTweetsDataSource? =
        searchTweetsDataSourceFactory.searchTweetsDataSourceLiveData.value

    companion object {
        const val PAGE_SIZE = 20
    }
}
