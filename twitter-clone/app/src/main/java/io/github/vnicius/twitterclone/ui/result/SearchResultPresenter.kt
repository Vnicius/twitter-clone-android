package io.github.vnicius.twitterclone.ui.result

import android.util.Log
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepository
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepositoryRemote
import io.github.vnicius.twitterclone.utils.LogTagsUtils
import kotlinx.coroutines.*
import twitter4j.TwitterException
import java.lang.Exception

private const val MAX_COUNT = 50

/**
 * SearchResult Presenter
 */
class SearchResultPresenter(val view: SearchResultContract.View) : SearchResultContract.Presenter {

    // repository instance
    private val tweetRepository: TweetRepository = TweetRepositoryRemote()
    private val presenterJob = SupervisorJob()
    private val presenterScope = CoroutineScope(Dispatchers.Main + presenterJob)

    override fun searchTweets(query: String) {
        view.showLoader()

        presenterScope.launch {
            // search the tweets
            try {
                val result =
                    tweetRepository.getTweetsByQueryAsync(query, MAX_COUNT)

                // check if has any result
                if (result.size == 0) {
                    view.showNoResult()
                } else {
                    view.showResult(result)
                }
            } catch (e: TwitterException) {
                Log.e(LogTagsUtils.DEBUG_EXCEPTION, "Twitter connection exception", e)

                view.showConnectionErrorMessage()
            } catch (e: Exception) {
                Log.e(LogTagsUtils.DEBUG_EXCEPTION, "Unknown exception", e)

                view.showError("Connection Error")
            }
        }
    }

    override fun dispose() {
        presenterScope.coroutineContext.cancelChildren()
    }
}
