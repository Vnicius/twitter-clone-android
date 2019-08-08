package io.github.vnicius.twitterclone.ui.main

import io.github.vnicius.twitterclone.api.APIInterface
import io.github.vnicius.twitterclone.api.TwitterAPI
import kotlinx.coroutines.*

class MainPresenter(val view: MainContract.View): MainContract.Presenter {

    private val api: APIInterface = TwitterAPI()

    override fun searchTweets(query: String) {
        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch {
            val result = api.search(query).await()
            view.showResult(result)
        }
    }
}