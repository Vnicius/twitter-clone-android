package io.github.vnicius.twitterclone.data.datasource.searchtweets

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.github.vnicius.twitterclone.data.model.UserStatus
import io.github.vnicius.twitterclone.data.repository.RepositoryFactory
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepository
import twitter4j.Query
import twitter4j.Status

class SearchTweetsDataSourceFactory(
    val queryText: String,
    val pageSize: Int,
    val tweetsRepository: RepositoryFactory<TweetRepository>
) : DataSource.Factory<Query, UserStatus>() {

    val searchTweetsDataSourceLiveData = MutableLiveData<SearchTweetsDataSource>()

    override fun create(): DataSource<Query, UserStatus> {
        val searchTweetsDataSource = SearchTweetsDataSource(queryText, pageSize, tweetsRepository)
        searchTweetsDataSourceLiveData.postValue(searchTweetsDataSource)
        return searchTweetsDataSource
    }
}