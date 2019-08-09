package io.github.vnicius.twitterclone.ui.main

import io.github.vnicius.twitterclone.api.APIInterface
import io.github.vnicius.twitterclone.api.TwitterAPI
import io.github.vnicius.twitterclone.data.repository.trends.ITrendRepository
import io.github.vnicius.twitterclone.data.repository.trends.TrendRepository
import kotlinx.coroutines.*

class MainPresenter(val view: MainContract.View): MainContract.Presenter {

    private val mTrendRepository: ITrendRepository = TrendRepository()

    override fun getTrends() {
        val scope = CoroutineScope(Dispatchers.Main)
        view.showLoader()

        view.showLoader()
        scope.launch {
            val trends = mTrendRepository.getTrends(1).await()
            view.showTrends(trends)
        }
    }

}