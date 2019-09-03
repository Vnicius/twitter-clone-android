package io.github.vnicius.twitterclone.ui.result

import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepository
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepositoryRemote
import kotlinx.coroutines.*
import java.lang.Exception

private const val MAX_COUNT = 50

/**
 * SearchResult Presenter
 */
class SearchResultPresenter(val view: SearchResultContract.View) : SearchResultContract.Presenter {

    // repository instance
    private val mTweetRepository: TweetRepository = TweetRepositoryRemote()
    private val presenterJob = SupervisorJob()
    private val presenterScope = CoroutineScope(Dispatchers.Main + presenterJob)

    override fun searchTweets(query: String) {
        view.showLoader()
        presenterScope.launch {
            // search the tweets
            try {
                coroutineScope {
                    val result = mTweetRepository.getTweetsByQuery(query, MAX_COUNT).await()

                    // check if has any result
                    if (result.size == 0) {
                        view.showNoResult()
                    } else {
                        view.showResult(result)
                    }
                }
            } catch (e: Exception) {
                view.showError("Connection Error")
            }
        }
    }

    override fun dispose() {
        presenterScope.coroutineContext.cancelChildren()
    }
}