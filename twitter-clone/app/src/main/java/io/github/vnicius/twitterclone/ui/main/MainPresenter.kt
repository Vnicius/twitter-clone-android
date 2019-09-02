package io.github.vnicius.twitterclone.ui.main

import io.github.vnicius.twitterclone.data.repository.trends.TrendRepository
import io.github.vnicius.twitterclone.data.repository.trends.TrendRepositoryRemote
import kotlinx.coroutines.*
import twitter4j.Trend

/**
 * Main Presenter
 * @property view the instance of the view
 */
class MainPresenter(val view: MainContract.View): MainContract.Presenter {

    // instance of the repository
    private val mTrendRepository: TrendRepository = TrendRepositoryRemote()

    override fun getTrends() {
        // get the main scope
        val scope = CoroutineScope(Dispatchers.Main)

        view.showLoader()
        scope.launch {
            // get the trends
            var trends: Array<Trend>
            try{
                coroutineScope {
                    trends = mTrendRepository.getTrends(1).await()

                    // show the trends
                    view.showTrends(trends)
                }
            }catch (e: Exception) {
                view.showError("Connection Error")
            }
        }
    }

}