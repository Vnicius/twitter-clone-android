package io.github.vnicius.twitterclone.ui.main

import android.util.Log
import io.github.vnicius.twitterclone.data.repository.trends.TrendRepository
import io.github.vnicius.twitterclone.data.repository.trends.TrendRepositoryRemote
import io.github.vnicius.twitterclone.utils.LogTagsUtils
import kotlinx.coroutines.*
import twitter4j.TwitterException

/**
 * Main Presenter
 * @property view the instance of the view
 */
class MainPresenter(val view: MainContract.View) : MainContract.Presenter {

    private val trendRepository: TrendRepository = TrendRepositoryRemote()
    private var presenterJob = SupervisorJob()
    private val presenterScope = CoroutineScope(Dispatchers.Main + presenterJob)

    override fun getTrends() {
        view.showLoader()

        presenterScope.launch {
            try {
                val trends = trendRepository.getTrendsAsync(1)

                view.showTrends(trends)
            } catch (e: TwitterException) {
                Log.e(LogTagsUtils.DEBUG_EXCEPTION, "Twitter connection exception", e)

                view.showConnectionErrorMessage()
            } catch (e: Exception) {
                Log.e(LogTagsUtils.DEBUG_EXCEPTION, "Unknown exception", e)

                view.showError("Some error occurred")
            }
        }
    }

    override fun dispose() {
        presenterScope.coroutineContext.cancelChildren()
    }
}
