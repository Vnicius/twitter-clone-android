package io.github.vnicius.twitterclone.data.datasource.searchtweets

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.github.vnicius.twitterclone.data.repository.Repository
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepository
import twitter4j.Query
import twitter4j.Status

class SearchTweetsDataSourceFactory(
    val queryText: String,
    val pageSize: Int,
    val tweetsRepository: Repository<TweetRepository>
) : DataSource.Factory<Query, Status>() {

    val searchTweetsDataSourceLiveData = MutableLiveData<SearchTweetsDataSource>()

    override fun create(): DataSource<Query, Status> {
        val searchTweetsDataSource = SearchTweetsDataSource(queryText, pageSize, tweetsRepository)
        searchTweetsDataSourceLiveData.postValue(searchTweetsDataSource)
        return searchTweetsDataSource
    }
}