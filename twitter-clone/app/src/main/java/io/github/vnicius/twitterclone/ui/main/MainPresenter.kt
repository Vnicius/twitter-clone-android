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
    private var presenterJob = SupervisorJob()
    private val presenterScope = CoroutineScope(Dispatchers.Main + presenterJob)

    override fun getTrends() {
        view.showLoader()

        presenterScope.launch {
            try {
                // get the trends
                val trends = mTrendRepository.getTrendsAsync(1)

                // show the trends
                view.showTrends(trends)
            } catch (t: TwitterException) {
                view.showConnectionErrorMessage()
            } catch (e: Exception) {
                Log.e("debug", e.toString(), e)
                view.showError("Some error occurred")
            }
        }
    }

    override fun dispose() {
        presenterScope.coroutineContext.cancelChildren()
    }
}