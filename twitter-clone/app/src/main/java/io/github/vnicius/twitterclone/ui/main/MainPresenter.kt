package io.github.vnicius.twitterclone.ui.main

import android.util.Log
import io.github.vnicius.twitterclone.api.APIInterface
import io.github.vnicius.twitterclone.api.TwitterAPI
import io.github.vnicius.twitterclone.data.repository.trends.ITrendRepository
import io.github.vnicius.twitterclone.data.repository.trends.TrendRepository
import kotlinx.coroutines.*
import twitter4j.Trend

class MainPresenter(val view: MainContract.View): MainContract.Presenter {

    private val mTrendRepository: ITrendRepository = TrendRepository()

    override fun getTrends() {
        val scope = CoroutineScope(Dispatchers.Main)
        view.showLoader()

        view.showLoader()
        scope.launch {
            var trends: Array<Trend>
            try{
                coroutineScope {
                    trends = mTrendRepository.getTrends(1).await()
                    view.showTrends(trends)
                }
            }catch (e: Exception) {
                view.showError("Connection Error")
            }
        }
    }

}