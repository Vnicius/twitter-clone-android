package io.github.vnicius.twitterclone.ui.main

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
            // get the trends
            var trends: Array<Trend>
            try {
                coroutineScope {
                    trends = mTrendRepository.getTrendsAsync(1).await()

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
        presenterScope.coroutineContext.cancelChildren()
    }
}