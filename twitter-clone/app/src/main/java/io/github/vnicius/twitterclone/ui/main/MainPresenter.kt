package io.github.vnicius.twitterclone.ui.main

import android.util.Log
import io.github.vnicius.twitterclone.data.repository.trends.TrendRepository
import io.github.vnicius.twitterclone.data.repository.trends.TrendRepositoryRemote
import kotlinx.coroutines.*
import twitter4j.Trend
import twitter4j.TwitterException

/**
 * Main Presenter
 * @property view the instance of the view
 */
class MainPresenter(val view: MainContract.View) : MainContract.Presenter {


    // instance of the repository
    private val mTrendRepository: TrendRepository = TrendRepositoryRemote()
    private lateinit var job: Job

    override fun getTrends() {
        // get the main scope
        val scope = CoroutineScope(Dispatchers.Main)

        view.showLoader()
        job = scope.launch {
            // get the trends
            var trends: Array<Trend>
            try {
                coroutineScope {
                    trends = mTrendRepository.getTrends(1).await()

                    // show the trends
                    view.showTrends(trends)
                }
            } catch (t: TwitterException) {
                view.showConnectionErrorMessage()
            } catch (e: Exception) {
                view.showError("Some error occurred")
            }
        }
    }

    override fun dispose() {
        job.cancel()
    }
}