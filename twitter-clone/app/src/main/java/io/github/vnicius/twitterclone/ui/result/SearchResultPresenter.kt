package io.github.vnicius.twitterclone.ui.result

import io.github.vnicius.twitterclone.data.repository.tweet.ITweetRepository
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

private const val MAX_COUNT = 50

/**
 * SearchResult Presenter
 */
class SearchResultPresenter(val view: SearchResultContract.View): SearchResultContract.Presenter {

    // repository instance
    private val mTweetRepository: ITweetRepository = TweetRepository()

    override fun searchTweets(query: String) {
        // get the main scope
        val scope = CoroutineScope(Dispatchers.Main)

        view.showLoader()
        scope.launch {
            // search the tweets
            try{
                coroutineScope {
                    val result = mTweetRepository.getTweetsByQuery(query, MAX_COUNT).await()

                    // check if has any result
                    if(result.size == 0) {
                        view.showNoResult()
                    } else {
                        view.showResult(result)
                    }
                }
            }catch (e: Exception) {
                view.showError("Connection Error")
            }
        }
    }
}