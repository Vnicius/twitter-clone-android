package io.github.vnicius.twitterclone.ui.result

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.github.vnicius.twitterclone.data.datasource.searchtweets.SearchTweetsDataSource
import io.github.vnicius.twitterclone.data.datasource.searchtweets.SearchTweetsDataSourceFactory
import io.github.vnicius.twitterclone.data.repository.Repository
import io.github.vnicius.twitterclone.data.repository.RepositoryFactory
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepository
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepositoryRemote
import io.github.vnicius.twitterclone.utils.State
import kotlinx.coroutines.launch
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
    lateinit var state: LiveData<State>
    lateinit var tweetsList: LiveData<PagedList<Status>>
    var localTweetsList: MutableLiveData<List<Status>?> = MutableLiveData()


    fun build(query: String) {
        fetchLocalTweets(query)

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

    private fun fetchLocalTweets(query: String) {
        viewModelScope.launch {
            localTweetsList.postValue(tweetRepository.local.getTweetsAsync(query))
        }
    }
}
